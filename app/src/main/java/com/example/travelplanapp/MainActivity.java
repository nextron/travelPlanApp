package com.example.travelplanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Spinner spCountry, spCity;
    TextView txvDailyExpenses, txvCityNameVisibility, txvDaysVisibility;
    Button btnAdd,btnRemove,btnBook;
    EditText edtDays;
    ListView listSelected;

    String[] countries = {"India","Canada","USA"};
    ArrayList<City> cities = new ArrayList<>();
    ArrayList<City> tempCities = new ArrayList<>();
    static int cityIndex = 0;
    static int selectedCityIndex = -1;
    HashMap<String, Integer> selectedCities = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing the view components
        spCountry = findViewById(R.id.spCountry);
        spCity = findViewById(R.id.spCity);
        txvDailyExpenses = findViewById(R.id.txvDailyExpenses);
        btnAdd = findViewById(R.id.btnAdd);
        edtDays = findViewById(R.id.edtDays);
        btnRemove = findViewById(R.id.btnRemove);
        listSelected = findViewById(R.id.listSelected);
        txvCityNameVisibility = findViewById(R.id.txcCityNameVisibility);
        txvDaysVisibility = findViewById(R.id.txvDaysVisibility);
        btnBook = findViewById(R.id.btnBook);

        //calling function to fill data
        fillData();

        ArrayAdapter aaCountry = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, countries);
        spCountry.setAdapter(aaCountry);
        //spCity.setAdapter(aaCountry);

        spCountry.setOnItemSelectedListener(new SpinnerEvents());
        spCity.setOnItemSelectedListener(new SpinnerEvents());

        btnAdd.setOnClickListener(new ButtonEvents());
        btnRemove.setOnClickListener(new ButtonEvents());
        btnBook.setOnClickListener(new ButtonEvents());

        listSelected.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCityIndex = position;
            }
        });
    }

    //implementing/handling buttons
    private class ButtonEvents implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.btnAdd){
                if(edtDays.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please provide the number of days you want to spend in: "+tempCities.get(cityIndex).getCity(), Toast.LENGTH_SHORT).show();
                }else{
                    if(checkCity(tempCities.get(cityIndex).getCity())){
                        Toast.makeText(MainActivity.this, "This city has been already been added, If you want to update please remove it and add it again.", Toast.LENGTH_LONG).show();
                    }else{
                        selectedCities.put(tempCities.get(cityIndex).getCity(),Integer.parseInt(edtDays.getText().toString()));
                        Toast.makeText(MainActivity.this, "City has been added to wishlist.", Toast.LENGTH_SHORT).show();
                        displaySelectedCities();
                        btnBook.setVisibility(View.VISIBLE);
                        btnRemove.setVisibility(View.VISIBLE);
                        txvCityNameVisibility.setVisibility(View.VISIBLE);
                        txvDaysVisibility.setVisibility(View.VISIBLE);
                    }
                }
            }else if(v.getId() == R.id.btnRemove){
                if(selectedCityIndex == -1){
                    Toast.makeText(MainActivity.this, "Please select a city to remove from your wishlist.", Toast.LENGTH_SHORT).show();
                }else{
                    int count = 0;
                    for(String ct: selectedCities.keySet()){
                        if(selectedCityIndex == count){
                            selectedCities.remove(ct);
                            selectedCityIndex = -1;
                            break;
                        }
                        count++;
                    }
                    if(selectedCities.size() == 0){
                        btnBook.setVisibility(View.INVISIBLE);
                        btnRemove.setVisibility(View.INVISIBLE);
                        txvCityNameVisibility.setVisibility(View.INVISIBLE);
                        txvDaysVisibility.setVisibility(View.INVISIBLE);
                    }
                    displaySelectedCities();
                    Toast.makeText(MainActivity.this, "Item has been removed from wishlist.", Toast.LENGTH_SHORT).show();
                }
            }else if(v.getId() == R.id.btnBook){
                double total = 0;
                for(String ct: selectedCities.keySet()){
                    for(City city: cities){
                        if(city.getCity().equals(ct)){
                            total += city.getAmount() * selectedCities.get(ct);
                        }
                    }
                }
                Toast.makeText(MainActivity.this, "Your booking has been confirmed with us and total amount is: "+ total +" $. Have a safe journey.", Toast.LENGTH_LONG).show();
                btnBook.setVisibility(View.INVISIBLE);
                btnRemove.setVisibility(View.INVISIBLE);
                txvCityNameVisibility.setVisibility(View.INVISIBLE);
                txvDaysVisibility.setVisibility(View.INVISIBLE);
                selectedCities.clear();
                displaySelectedCities();
            }
        }
    }

    //implementing/handling spinner events
    private class SpinnerEvents implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(parent.getId() == R.id.spCountry){
                String[] cityNames = getCities(countries[position]);
                System.out.println(cityNames[0]);
                ArrayAdapter aaCity = new ArrayAdapter(MainActivity.this, R.layout.support_simple_spinner_dropdown_item,cityNames);
                spCity.setAdapter(aaCity);
            }else if(parent.getId() ==  R.id.spCity){
                cityIndex = position;
                String expense = tempCities.get(cityIndex).getAmount() + " $";
                txvDailyExpenses.setText(expense);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    //to check if the city has been already added
    private boolean checkCity(String cityName){
        for(String city: selectedCities.keySet()){
            if(city.equals(cityName)){
                return true;
            }
        }
        return false;
    }

    //to display data in selected cities list
    private void displaySelectedCities(){
        int count = 0;
        String[] cities = new String[selectedCities.size()];
        for(String city: selectedCities.keySet()){
            String days = String.valueOf(selectedCities.get(city));
            String spaces = "";
            for(int i = 0; i < 32-city.length()-days.length() ; i++){
                spaces += " ";
            }
            cities[count] =  city + spaces +selectedCities.get(city);
            count++;
        }
        ArrayAdapter aaSelectedCities = new ArrayAdapter(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,cities);
        listSelected.setAdapter(aaSelectedCities);
    }

    //filling cities array list
    private void fillData(){
        cities.add(new City(countries[0],"Kerala",100.5));
        cities.add(new City(countries[0],"Goa",150.45));
        cities.add(new City(countries[0],"Delhi",125.55));
        cities.add(new City(countries[1],"Toronto",350.5));
        cities.add(new City(countries[1],"Montreal",325.15));
        cities.add(new City(countries[1],"Vancouver",310.25));
        cities.add(new City(countries[2],"New York",370.75));
        cities.add(new City(countries[2],"Los Angeles",360.75));
        cities.add(new City(countries[2],"Miami",365.71));
    }

    //get cities Array with country as a input and filling tempCities List
    private String[] getCities(String countryName){
        tempCities.clear();
        for(City ct: cities){
            if(ct.getCountry().equals(countryName)){
                tempCities.add(ct);
            }
        }
        String[] cityNames = new String[tempCities.size()];
        for(int i = 0; i < tempCities.size(); i++){
            cityNames[i] = tempCities.get(i).getCity();
        }
        return cityNames;
    }
}