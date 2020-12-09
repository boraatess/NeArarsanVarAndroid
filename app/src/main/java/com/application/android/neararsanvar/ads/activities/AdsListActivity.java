package com.application.android.neararsanvar.ads.activities;

/*-------------------------------

    - Classifier -

    Created by CarbonCode Technology @2019
    All Rights reserved.

-------------------------------*/

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.application.android.neararsanvar.filters.CategoriesActivity;
import com.application.android.neararsanvar.filters.CityActivity;
import com.application.android.neararsanvar.filters.TypeActivity;
import com.application.android.neararsanvar.filters.models.TypeBy;
import com.application.android.neararsanvar.home.adapters.AdsListScreenPagerAdapter;
import com.application.android.neararsanvar.home.adapters.BottomNavigationAdapter;
import com.application.android.neararsanvar.selledit.activities.SellEditItemActivity;
import com.application.android.neararsanvar.utils.Configs;

import com.application.android.neararsanvar.R;
import com.application.android.neararsanvar.filters.SortByActivity;
import com.application.android.neararsanvar.common.activities.BaseActivity;
import com.application.android.neararsanvar.filters.models.SortBy;
import com.application.android.neararsanvar.home.adapters.AdsListAdapter;
import com.application.android.neararsanvar.utils.Constants;
import com.application.android.neararsanvar.utils.SessionUtils;
import com.application.android.neararsanvar.utils.ToastUtils;
import com.application.android.neararsanvar.utils.UIUtils;
import com.application.android.neararsanvar.wizard.WizardActivity;

//public class AdsListActivity extends BaseActivity implements LocationListener,BottomNavigationAdapter.BottomNavigationListener {
public class AdsListActivity extends BaseActivity implements BottomNavigationAdapter.BottomNavigationListener {


    public static final String CATEGORY_NAME_KEY = "CATEGORY_NAME_KEY";
    public static final String SUBCATEGORY_NAME_KEY = "SUBCATEGORY_NAME_KEY";
    public static final String SEARCH_QUERY_KEY = "SEARCH_QUERY_KEY";
    public static final String CITY_NAME_KEY = "CITY_NAME_KEY";

    private static final int LOCATION_PERMISSIONS_REQUEST_CODE = 1;
    private static final int CATEGORY_REQ_CODE = 2;
    private static final int SORT_BY_REQ_CODE = 3;
    private static final int SORT_BY_TYPE_CODE = 5;
    //private static final int SET_LOCATION_REQ_CODE = 4;
    private static final int CITIES_REQ_CODE = 4;

  /*  private String[] locationPermissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};*/

    private EditText searchTxt;
    private TextView categoryTV, sortByTV, cityTV, typeTv;
    //private TextView subcategoryTV;
    private RelativeLayout categoryButtonRL, sortByButtonRL, cityCountryButtonRL, type;
    private TextView noSearchTxt;
    private RelativeLayout noResultsLayout;
    private ImageView backBtn;
    //private AdView adView;
    //private ImageView chatButt;
    private RecyclerView adsRV;
    private RecyclerView bottomNavigationRV;
    private LinearLayout alOptionsLayout;

    private String searchString;
    private String selectedCategory;
    private String selectedCity = Constants.BrowseCities.DEFAULT_CITIES_NAME;
    private String selectedSubcategory;
    private SortBy sortBy;
    private TypeBy typeBy;

    private Location currentLocation;
    private LocationManager locationManager;
    private float distanceInMiles = Configs.distanceInMiles;

    // Pagination
    private AdsListAdapter adsListAdapter;
    private ParseQuery<ParseObject> lastAdsQuery;
    private int querySkip = 0;
    private boolean isNextPageLoading;
    private boolean allAdsLoaded;
    private RelativeLayout topView;

    private BottomNavigationAdapter bottomNavigationAdapter;
    private static final int INITIAL_SELECTED_TAB_POSITION = 0;
    private AdsListScreenPagerAdapter pagerAdapter;
    private ViewPager contentVP;

    private boolean isUserLoggedIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_list);
        isUserLoggedIn = SessionUtils.isUserLoggedIn();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            searchString = extras.getString(SEARCH_QUERY_KEY);
            selectedCategory = extras.getString(CATEGORY_NAME_KEY);
            selectedSubcategory = extras.getString(SUBCATEGORY_NAME_KEY);
        }

        typeBy = TypeBy.ALL;
        //sortBy = SortBy.RECENT;
        sortBy = SortBy.NEWEST;

        Log.i("log-", "SEARCH TEXT STRING: " + searchString +
                "\nCATEGORY: " + selectedCategory + "\nSUBCATEGORY" + selectedSubcategory+ "\nCITY" + selectedCity);

        initViews();
        setUpViews();


        queryAds();

       /* ActivityCompat.requestPermissions(this, locationPermissions, LOCATION_PERMISSIONS_REQUEST_CODE);

        if (PermissionsUtils.hasPermissions(this, locationPermissions)) {
          //  loadAdsFromChosenLocation();
        } else {
            //queryAds();
            //ActivityCompat.requestPermissions(this, locationPermissions, LOCATION_PERMISSIONS_REQUEST_CODE);
        }*/
    }

    private void setUpViewPager() {
        pagerAdapter = new AdsListScreenPagerAdapter(getSupportFragmentManager());
        contentVP.setAdapter(pagerAdapter);
        contentVP.setOffscreenPageLimit(1);
    }

    private void setUpBottomNavigation() {
        ViewTreeObserver vto = bottomNavigationRV.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setUpBottomNavigationRV();
                bottomNavigationRV.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }


    private void setUpBottomNavigationRV() {
        bottomNavigationAdapter = new BottomNavigationAdapter(this, INITIAL_SELECTED_TAB_POSITION);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        bottomNavigationRV.setAdapter(bottomNavigationAdapter);
        bottomNavigationRV.setLayoutManager(layoutManager);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CATEGORY_REQ_CODE:
                    selectedCategory = data.getStringExtra(CategoriesActivity.SELECTED_CATEGORY_EXTRA_KEY);
                    selectedSubcategory = data.getStringExtra(CategoriesActivity.SELECTED_SUBCATEGORY_EXTRA_KEY);
                    if (selectedCategory.matches(Constants.BrowseCategories.DEFAULT_CATEGORY_NAME) ){
                        categoryTV.setText(selectedCategory);
                    }
                    if (!selectedCategory.matches(Constants.BrowseCategories.DEFAULT_CATEGORY_NAME) && selectedSubcategory.matches(Constants.BrowseSubcategories.DEFAULT_SUBCATEGORY_NAME) ){
                        categoryTV.setText(selectedCategory + " (" + Constants.BrowseSubcategories.DEFAULT_SUBCATEGORY_NAME + ")");
                    }
                    if (!selectedCategory.matches(Constants.BrowseCategories.DEFAULT_CATEGORY_NAME) && !selectedSubcategory.matches(Constants.BrowseSubcategories.DEFAULT_SUBCATEGORY_NAME)){
                        categoryTV.setText(selectedSubcategory);
                    }
                    //categoryTV.setText(selectedCategory);
                    searchTxt.setText(selectedCategory);
                    //subcategoryTV.setText(selectedSubcategory != null ? selectedSubcategory : getString(R.string.categories_all_title));
                    queryAds();
                    break;
                case SORT_BY_REQ_CODE:
                    String sortByValue = data.getStringExtra(SortByActivity.SELECTED_SORT_BY_EXTRA_KEY);
                    sortBy = SortBy.getForValue(sortByValue);
                    if (sortBy != null) {
                        sortByTV.setText(sortBy.getValue());
                    }
                    queryAds();
                    break;

                case SORT_BY_TYPE_CODE:
                    String typeByValue = data.getStringExtra(TypeActivity.SELECTED_TYPE_EXTRA_KEY);
                    typeBy = TypeBy.getForValue(typeByValue);
                    if (typeBy != null) {
                        typeTv.setText(typeBy.getValue());
                    }
                    queryAds();
                    break;

                case CITIES_REQ_CODE:
                    selectedCity = data.getStringExtra(CityActivity.SELECTED_CITIES_EXTRA_KEY);
                    cityTV.setText(selectedCity);
                    queryAds();
                    break;

                /*case SET_LOCATION_REQ_CODE:
                    Location chosenLocation = data.getParcelableExtra(DistanceMapActivity.LOCATION_EXTRA_KEY);
                    if (chosenLocation != null) {
                        currentLocation = chosenLocation;
                    }
                    distanceInMiles = data.getFloatExtra(DistanceMapActivity.DISTANCE_EXTRA_KEY, Configs.distanceInMiles);
                  //  loadAdsFromChosenLocation();
                    break;*/
            }
        }
    }

    //@Override
    //public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    //    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    //    /*if (requestCode == LOCATION_PERMISSIONS_REQUEST_CODE) {
    //        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
    //            //loadAdsFromChosenLocation();
    //        }
    //    }*/
    //}

    private void initViews() {
        searchTxt = findViewById(R.id.alSearchTxt);
        //distanceTxt = findViewById(R.id.alDistanceTxt);
        categoryTV = findViewById(R.id.alCategoryTV);
        //subcategoryTV = findViewById(R.id.alSubcategoryTV);
        sortByTV = findViewById(R.id.alSortByTV);
        cityTV = findViewById(R.id.alCityTV);
        noSearchTxt = findViewById(R.id.alNoSearchTxt);
        noResultsLayout = findViewById(R.id.alNoResultsLayout);
        backBtn = findViewById(R.id.alBackButt);
        //adView = findViewById(R.id.admobBanner);
        //chatButt = findViewById(R.id.alChatButt);
        adsRV = findViewById(R.id.aal_ads_rv);
        categoryButtonRL = findViewById(R.id.aal_category_rl);
        sortByButtonRL = findViewById(R.id.aal_sort_rl);
        cityCountryButtonRL = findViewById(R.id.aal_city_rl);
        type = findViewById(R.id.aal_type_rl);
        typeTv = findViewById(R.id.typeTV);
        bottomNavigationRV = findViewById(R.id.ah_bottom_navigation_rv);
        contentVP = findViewById(R.id.ah_content_vp_adlist);
        topView= findViewById(R.id.alTopView);
        alOptionsLayout= findViewById(R.id.alOptionsLayout);
    }

    private void setUpViews() {
        searchTxt.setTypeface(Configs.titRegular);
        //distanceTxt.setTypeface(Configs.titRegular);
        categoryTV.setTypeface(Configs.titSemibold);
        typeTv.setTypeface(Configs.titSemibold);
        sortByTV.setTypeface(Configs.titSemibold);
        cityTV.setTypeface(Configs.titSemibold);
        noSearchTxt.setTypeface(Configs.titSemibold);
        noResultsLayout.setVisibility(View.GONE);

        setUpViewPager();
        setUpBottomNavigation();

        // Set search variables for the query
        if (searchString != null) {
            searchTxt.setText(searchString);
        } else {
            searchTxt.setText(selectedCategory);
        }
        categoryTV.setText(selectedSubcategory);
        //subcategoryTV.setText(selectedSubcategory);

        // Set sort By text
        sortByTV.setText(sortBy.getValue());

        // Set sort By text
        typeTv.setText(typeBy.getValue());


        // MARK: - SEARCH ADS BY KEYWORDS --------------------------------------------------------
        searchTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (!searchTxt.getText().toString().matches("")) {

                        selectedCategory = Constants.BrowseCategories.DEFAULT_CATEGORY_NAME;
                        selectedCity = Constants.BrowseCities.DEFAULT_CITIES_NAME;

                        selectedSubcategory = Constants.BrowseSubcategories.DEFAULT_SUBCATEGORY_NAME;

                        categoryTV.setText(selectedCategory);
                        cityTV.setText(selectedCity);

                        //subcategoryTV.setText(selectedSubcategory);
                        searchString = searchTxt.getText().toString();
                        UIUtils.hideKeyboard(searchTxt);

                        // Call query
                        queryAds();

                        return true;
                    }

                    // No text -> No search
                } else {
                    ToastUtils.showMessage(getString(R.string.ads_list_search_validation_error));
                }

                return false;
            }
        });

        // EditText TextWatcher delegate
        searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int closeDrawable;
                if (s.length() == 0) {
                    closeDrawable = 0;
                } else {
                    closeDrawable = R.drawable.close_white_ic;
                }
                searchTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, closeDrawable, 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        final Drawable imgClearButton = ContextCompat.getDrawable(this, R.drawable.close_white_ic);
        searchTxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (searchTxt.getText().length() > 0) {
                    if (event.getX() > searchTxt.getWidth() - searchTxt.getPaddingRight() - imgClearButton.getIntrinsicWidth()) {
                        searchTxt.setText("");
                        searchString = null;
                        selectedCategory = Constants.BrowseCategories.DEFAULT_CATEGORY_NAME;
                        selectedSubcategory = Constants.BrowseSubcategories.DEFAULT_SUBCATEGORY_NAME;
                        selectedCity = Constants.BrowseCities.DEFAULT_CITIES_NAME;

                        categoryTV.setText(selectedCategory);
                        cityTV.setText(selectedCity);
                        //subcategoryTV.setText(selectedSubcategory);

                        queryAds();
                        return true;
                    }
                }
                return false;
            }
        });

        // MARK: - CHATS BUTTON ------------------------------------
        //chatButt.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        if (ParseUser.getCurrentUser().getUsername() != null) {
        //            startActivity(new Intent(AdsListActivity.this, Chats.class));
        //        } else {
        //            Configs.loginAlert(getString(R.string.browse_chat_error), AdsListActivity.this);
        //        }
        //    }
        //});

        // MARK: - CITY/COUNTRY BUTTON ------------------------------------
      /*  cityCountryButtonRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("log-", "CURR LOCATION: " + currentLocation);

//                // if (!distanceTxt.getText().toString().matches("loading...")) {
//                if (currentLocation != null) {
//                    Double lat = currentLocation.getLatitude();
//                    Double lng = currentLocation.getLongitude();
//                    Log.i("log-", "LATITUDE: " + lat + "\nLONGITUDE: " + lng);
//                    Intent i = new Intent(AdsListActivity.this, DistanceMapActivity.class);
//                    Bundle extras = new Bundle();
//                    extras.putDouble("latitude", lat);
//                    extras.putDouble("longitude", lng);
//                    i.putExtras(extras);
//                    startActivityForResult(i, SET_LOCATION_REQ_CODE);
//
//                } else {
//                    // Set default Location
//                    Intent i = new Intent(AdsListActivity.this, DistanceMapActivity.class);
//                    Bundle extras = new Bundle();
//                    extras.putDouble("latitude", Configs.DEFAULT_LOCATION.latitude);
//                    extras.putDouble("longitude", Configs.DEFAULT_LOCATION.longitude);
//                    i.putExtras(extras);
//                    startActivityForResult(i, SET_LOCATION_REQ_CODE);
//                }
                // }
            }
        });*/


        // MARK: - CHOOSE CITY BUTTON ------------------------------------
        cityCountryButtonRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent citiesIntent = new Intent(AdsListActivity.this, CityActivity.class);
                citiesIntent.putExtra(CityActivity.SELECTED_CITIES_EXTRA_KEY, selectedCity);
                startActivityForResult(citiesIntent, CITIES_REQ_CODE);
            }
        });


        // MARK: - CHOOSE CATEGORY BUTTON ------------------------------------
        categoryButtonRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent categoriesIntent = new Intent(AdsListActivity.this, CategoriesActivity.class);
                //categoriesIntent.putExtra(CategoriesActivity.SELECTED_CATEGORY_EXTRA_KEY, selectedCategory);
                startActivityForResult(categoriesIntent, CATEGORY_REQ_CODE);
            }
        });


        // MARK: - CHOOSE TYPE BUTTON ------------------------------------
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent typeIntent = new Intent(AdsListActivity.this, TypeActivity.class);
                if (typeBy != null) {
                    typeIntent.putExtra(TypeActivity.SELECTED_TYPE_EXTRA_KEY, typeBy.getValue());
                }
                startActivityForResult(typeIntent, SORT_BY_TYPE_CODE);
            }
        });


        // MARK: - SORT BY BUTTON ------------------------------------
        sortByButtonRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sortIntent = new Intent(AdsListActivity.this, SortByActivity.class);
                if (sortBy != null) {
                    sortIntent.putExtra(SortByActivity.SELECTED_SORT_BY_EXTRA_KEY, sortBy.getValue());
                }
                startActivityForResult(sortIntent, SORT_BY_REQ_CODE);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Init AdMob banner
        //AdRequest adRequest = new AdRequest.Builder().build();
        //adView.loadAd(adRequest);
    }

    //private void loadAdsFromChosenLocation() {
    //    // Get ads from a chosen location
    //    if (currentLocation != null) {
    //        getCityCountryNames();
    //    } else {
    //        getCurrentLocation();
    //    }
    //}
//
    //// MARK: - GET CURRENT LOCATION ------------------------------------------------------
    //protected void getCurrentLocation() {
    //    Criteria criteria = new Criteria();
    //    criteria.setAccuracy(Criteria.ACCURACY_LOW);
    //    criteria.setPowerRequirement(Criteria.POWER_LOW);
    //    criteria.setAltitudeRequired(false);
    //    criteria.setBearingRequired(false);
//
    //    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    //    assert locationManager != null;
    //    String provider = locationManager.getBestProvider(criteria, true);

       /* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }*/
       // currentLocation = locationManager.getLastKnownLocation(provider);

        /*if (currentLocation != null) {
            getCityCountryNames();

            // Try to find your current Location one more time
        } else {
            locationManager.requestLocationUpdates(provider, 1000, 0, this);
        }*/
    //}


    //@Override
    //public void onLocationChanged(Location location) {
    //    /*if (PermissionsUtils.hasPermissions(this, locationPermissions)) {
    //        return;
    //    }*/
//
    //    locationManager.removeUpdates(this);
    //    currentLocation = location;
//
    //    if (currentLocation != null) {
    //        getCityCountryNames();
    //        // NO GPS location found!
    //    } else {
    //        Configs.simpleAlert(getString(R.string.get_location_failure), AdsListActivity.this);
//
    //        // Set New York City as default currentLocation
    //        currentLocation = new Location("provider");
    //        currentLocation.setLatitude(Configs.DEFAULT_LOCATION.latitude);
    //        currentLocation.setLongitude(Configs.DEFAULT_LOCATION.longitude);
//
    //        // Set distance and city labels
    //        String distFormatted = String.format("%.0f", distanceInMiles);
    //        //distanceTxt.setText(getString(R.string.ads_list_distance_formatted, distFormatted));
    //        cityCountryTV.setText(getString(R.string.not_available_text_placeholder));
//
    //        // Call query
    //        queryAds();
    //    }
    //}

    //@Override
    //public void onStatusChanged(String provider, int status, Bundle extras) {
    //}
//
    //@Override
    //public void onProviderEnabled(String provider) {
    //}
//
    //@Override
    //public void onProviderDisabled(String provider) {
    //}
//
    //// MARK: - GET CITY AND COUNTRY NAMES | CALL QUERY ADS -----------------------------------
    //private void getCityCountryNames() {
    //    try {
    //        Geocoder geocoder = new Geocoder(AdsListActivity.this, Locale.getDefault());
    //        List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
    //        if (Geocoder.isPresent()) {
    //            Address returnAddress = addresses.get(0);
    //            String city = returnAddress.getLocality();
    //            String country = returnAddress.getCountryName();
//
    //            if (city == null) {
    //                city = "";
    //            }
    //            // Show City/Country
    //            cityCountryTV.setText(city + ", " + country);
//
    //            // Set distance
    //            String distFormatted = String.format("%.0f", distanceInMiles);
    //            //distanceTxt.setText(getString(R.string.ads_list_distance_formatted, distFormatted));
//
    //            // Call query
    //            queryAds();
//
    //        } else {
    //            Toast.makeText(getApplicationContext(), R.string.geocoder_not_present_error, Toast.LENGTH_SHORT).show();
    //        }
    //    } catch (IOException e) {
    //        ToastUtils.showMessage(e.getMessage());
    //    }
    //}

    // MARK: - QUERY ADS ------------------------------------------------------------------
    private void queryAds() {
        noResultsLayout.setVisibility(View.GONE);
        showLoading(AdsListActivity.this);

        // Launch query
        lastAdsQuery = ParseQuery.getQuery(Configs.ADS_CLASS_NAME);
        // Reset to first page
        querySkip = 0;
        allAdsLoaded = false;

        // query by search text
        if (searchString != null) {
            // Get keywords
            String[] one = searchString.toLowerCase().split(" ");
            List<String> keywords = new ArrayList<>(Arrays.asList(one));
            Log.d("KEYWORDS", "\n" + keywords + "\n");

            lastAdsQuery.whereContainedIn(Configs.ADS_KEYWORDS, keywords);
        }


        // query by Category
        if (!selectedCategory.matches(Constants.BrowseCategories.DEFAULT_CATEGORY_NAME) && selectedSubcategory.matches(Constants.BrowseSubcategories.DEFAULT_SUBCATEGORY_NAME)){
            lastAdsQuery.whereEqualTo(Configs.ADS_CATEGORY, selectedCategory);
        }

        // query by Subcategory
        if (!selectedCategory.matches(Constants.BrowseCategories.DEFAULT_CATEGORY_NAME) && !selectedSubcategory.matches(Constants.BrowseSubcategories.DEFAULT_SUBCATEGORY_NAME)) {
            lastAdsQuery.whereEqualTo(Configs.ADS_SUBCATEGORY, selectedSubcategory);
        }

        // query by City
        if (!selectedCity.matches(Constants.BrowseCities.DEFAULT_CITIES_NAME)) {
            lastAdsQuery.whereEqualTo(Configs.ADS_CITIES, selectedCity);
        }

        //if (!TextUtils.isEmpty(selectedSubcategory) && !selectedSubcategory.matches(Constants.BrowseSubcategories.DEFAULT_SUBCATEGORY_NAME)) {
        //    lastAdsQuery.whereEqualTo(Configs.ADS_SUBCATEGORY, selectedSubcategory);
        //}

        /*// query nearby
        if (currentLocation != null) {
            ParseGeoPoint gp = new ParseGeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
            lastAdsQuery.whereWithinMiles(Configs.ADS_LOCATION, gp, distanceInMiles);
        }*/

        // query sortBy
        switch (sortBy) {
            //case RECENT:
            //    lastAdsQuery.orderByDescending(Configs.ADS_CREATED_AT);
            //    break;
            case NEWEST:
                lastAdsQuery.orderByDescending(Configs.ADS_CREATED_AT);
                //lastAdsQuery.whereEqualTo(Configs.ADS_CONDITION, getString(R.string.filter_condition_new));
                break;
            case OLDEST:
                lastAdsQuery.orderByAscending(Configs.ADS_CREATED_AT);
                //lastAdsQuery.whereEqualTo(Configs.ADS_CONDITION, getString(R.string.filter_condition_used));
                break;
            case LOWEST_PRICE:
                lastAdsQuery.orderByAscending(Configs.ADS_PRICE);
                break;
            case HIGHEST_PRICE:
                lastAdsQuery.orderByDescending(Configs.ADS_PRICE);
                break;
            case MOST_LIKED:
                lastAdsQuery.orderByDescending(Configs.ADS_LIKES);
                break;
            default:
                break;
        }



        // query typeBy
        switch (typeBy) {
            case ALL:
                lastAdsQuery.whereNotEqualTo(Configs.ADS_CONDITION, "PoweredByCarbonCodeTechnology");
                break;
            case SELL:
                lastAdsQuery.whereEqualTo(Configs.ADS_CONDITION, getString(R.string.type_sell));
                break;
            case RENT:
                lastAdsQuery.whereEqualTo(Configs.ADS_CONDITION, getString(R.string.type_rent));
                break;
            case EXCHANGE:
                lastAdsQuery.whereEqualTo(Configs.ADS_CONDITION, getString(R.string.type_exchange));
                break;
            case GIFT:
                lastAdsQuery.whereEqualTo(Configs.ADS_CONDITION, getString(R.string.type_gift));
                break;
            default:
                break;
        }

        //lastAdsQuery.whereEqualTo(Configs.ADS_IS_REPORTED, false);
        lastAdsQuery.setLimit(Configs.DEFAULT_PAGE_SIZE);
        lastAdsQuery.setSkip(querySkip);

        lastAdsQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException error) {
                if (error == null) {
                    hideLoading();
                    setUpAdList(objects);
                } else {
                    hideLoading();
                    ToastUtils.showMessage(error.getMessage());
                }
            }
        });
    }

    private void loadMoreAds() {
        if (lastAdsQuery == null) {
            Log.d("log-", "Last query is null");
            return;
        }

        if (isNextPageLoading) {
            Log.d("log-", "Next page is loading");
            return;
        }

        if (allAdsLoaded) {
            Log.d("log-", "All ads are already loaded");
            return;
        }

        isNextPageLoading = true;
        querySkip += Configs.DEFAULT_PAGE_SIZE;
        lastAdsQuery.setLimit(Configs.DEFAULT_PAGE_SIZE);
        lastAdsQuery.setSkip(querySkip);
        lastAdsQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                isNextPageLoading = false;
                if (e == null) {
                    if (objects.isEmpty()) {
                        allAdsLoaded = true;
                        return;
                    }
                    adsListAdapter.addMoreAds(objects);
                } else {
                    Log.d("log-", "Error loading more ads: " + e.getMessage());
                }
            }
        });
    }

    private void setUpAdList(List<ParseObject> adList) {
        // Show/hide noResult view
        if (adList.isEmpty()) {
            noResultsLayout.setVisibility(View.VISIBLE);
            if (adsListAdapter != null) {
                adsListAdapter.clearAds();
            }
        } else {
            noResultsLayout.setVisibility(View.GONE);

            final GridLayoutManager layoutManager = new GridLayoutManager(AdsListActivity.this, 2);

            adsListAdapter = new AdsListAdapter(adList, new AdsListAdapter.OnAdClickListener() {
                @Override
                public void onAdClicked(ParseObject adObj) {
                    Intent adDetailsIntent = new Intent(AdsListActivity.this, AdDetailsActivity.class);
                    adDetailsIntent.putExtra(AdDetailsActivity.AD_OBJ_ID_KEY, adObj.getObjectId());
                    startActivity(adDetailsIntent);
                }
            });

            adsRV.setLayoutManager(layoutManager);
            adsRV.setAdapter(adsListAdapter);
            adsRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int totalItemCount = layoutManager.getItemCount();
                    int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                    if (lastVisibleItem != -1 &&
                            lastVisibleItem + Configs.DEFAULT_PAGE_THRESHOLD >= totalItemCount) {
                        Log.d("log-", "Toplam tutar: " + totalItemCount +
                                " en son görüntülenen: " + lastVisibleItem + " Daha fazla yükle");
                        loadMoreAds();
                    }
                }
            });
        }
    }

    @Override
    public boolean onTabSelected(int pos) {
        if (!isUserLoggedIn && pos != INITIAL_SELECTED_TAB_POSITION) {
            startActivity(new Intent(this, WizardActivity.class));
            return false;
        }
        if (pos>0){
            topView.setVisibility(View.GONE);
            noResultsLayout.setVisibility(View.GONE);
            adsRV.setVisibility(View.GONE);
            //adView.setVisibility(View.GONE);
            alOptionsLayout.setVisibility(View.GONE);
        }else {
            topView.setVisibility(View.VISIBLE);
            noResultsLayout.setVisibility(View.GONE);
            adsRV.setVisibility(View.VISIBLE);
            //adView.setVisibility(View.VISIBLE);
            alOptionsLayout.setVisibility(View.VISIBLE);
        }
        contentVP.setCurrentItem(pos);
        return true;
    }



    @Override
    public void onSpecialTabSelected() {
        if (!isUserLoggedIn) {
            startActivity(new Intent(this, WizardActivity.class));
            return;
        }
        startActivity(new Intent(this, SellEditItemActivity.class));
        return;
    }
}
