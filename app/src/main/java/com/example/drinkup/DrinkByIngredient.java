package com.example.drinkup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.drinkup.models.Drink;
import com.example.drinkup.models.Ingredient;
import com.example.drinkup.models.Response;
import com.example.drinkup.repositories.DrinkRepository;
import com.example.drinkup.repositories.IDrinkRepository;
import com.example.drinkup.repositories.ResponseCallback;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.drinkup.GestioneFile.*;

public class DrinkByIngredient extends AppCompatActivity implements View.OnClickListener, ResponseCallback {

    private static final String TAG ="ActivityDrink" ;

    private IDrinkRepository drinkRepository;

    private List<Drink> drinksWithDrinksApi;
    private List<String> drinksPreferiti;
    private List<String> listaTemp;

    private Button button_Search;
    private EditText drinkDaCercare;
    private TextView nomeDrink;
    private TextView text_nome;
    private TextView text_gradazione;
    private TextView text_ingredienti;
    private TextView text_quantita;
    private TextView text_preparazione;
    private ImageView imageViewDownload;
    private CardView cardView_InfoDrink;

    private TextView textView_Nome_Drink;
    private TextView textView_Alchool_Drink;
    private TextView textView_Ingredienti_Drink;
    private TextView textView_QuantitaIngredienti_Drink;
    private TextView textView_Preparazione_Drink;

    private Button button_Successivo_Drink;
    private Button button_Precedente_Drink;
    private Button button_Salva_Preferito;

    private List<String> nomiDrink;
    public static int posizione = 999;

    public DrinkByIngredient() throws IOException {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        textView_Alchool_Drink = (TextView) findViewById(R.id.textView_Alchool_Drink);
        //text_nome = (TextView) findViewById(R.id.textView);
        cardView_InfoDrink=(CardView)findViewById(R.id.CardView_InfoDrink);
        text_gradazione = (TextView) findViewById(R.id.text_Gradazione);
        text_ingredienti= (TextView) findViewById(R.id.text_Ingredienti);
        text_preparazione= (TextView) findViewById(R.id.text_Preparazione);
        text_quantita= (TextView) findViewById(R.id.text_Quantita);
        //text_nome.setVisibility(View.INVISIBLE);
        text_gradazione.setVisibility(View.INVISIBLE);
        text_ingredienti.setVisibility(View.INVISIBLE);
        text_preparazione.setVisibility(View.INVISIBLE);
        text_quantita.setVisibility(View.INVISIBLE);
        cardView_InfoDrink.setVisibility(View.INVISIBLE);

        textView_Ingredienti_Drink = (TextView) findViewById(R.id.textView_Ingredienti_Drink);
        textView_QuantitaIngredienti_Drink = (TextView) findViewById(R.id.textView_QuantitaIngredienti_Drink);
        textView_Preparazione_Drink = (TextView) findViewById(R.id.textView_Preparazione_Drink);
        textView_Nome_Drink = (TextView) findViewById(R.id.textView_Nome_Drink);

        imageViewDownload = (ImageView) findViewById(R.id.imageView_Drink);
        imageViewDownload.setVisibility(View.INVISIBLE);
        button_Search = (Button) findViewById(R.id.button_Search);
        button_Search.setOnClickListener(this);

        button_Successivo_Drink = (Button) findViewById(R.id.button_Successivo_Drink);
        button_Successivo_Drink.setOnClickListener(this);

        button_Precedente_Drink = (Button) findViewById(R.id.button_Precedente_Drink);
        button_Precedente_Drink.setOnClickListener(this);

        button_Salva_Preferito = (Button) findViewById(R.id.button_Salva_Preferito);
        button_Salva_Preferito.setOnClickListener(this);

        nomeDrink = (TextView) findViewById(R.id.textView_Alchool_Drink);
        drinkDaCercare = (EditText) findViewById(R.id.editTextText_DrinkSearch);

        drinkRepository = new DrinkRepository(this, this.getApplication());
        drinksWithDrinksApi=new ArrayList<>();
        drinksPreferiti=new ArrayList<>();
        nomiDrink = new ArrayList<>();
        button_Successivo_Drink.setVisibility(View.INVISIBLE);
        button_Precedente_Drink.setVisibility(View.INVISIBLE);

        try {
            RecuperaDrinkPreferiti();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //metodo che gestisce gli eventi di Click sui vari bottoni
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResponse(List<Drink> drinkList) {


    }

    @Override
    public void onFailure(String msg) {
        Toast toastOnFailure = Toast.makeText(this, "ERRORE!!", Toast.LENGTH_LONG);
        toastOnFailure.show();
    }

    // DA NON USARE
    @Override
    public void onResponseI(List<Ingredient> ingredientList) {

    }

    @Override
    public void onResponseNome(List<String> nomeDrink) {

    }

    private void imgGlide(String urlPassata){

        String url = urlPassata;
        String newUrl = null;

        if (url != null) {
            // This action is a possible alternative to manage HTTP addresses that don't work
            // in the apps that target API level 28 or higher.
            // If it doesn't work, the other option is this one:
            // https://developer.android.com/guide/topics/manifest/application-element#usesCleartextTraffic
            newUrl = url.replace("http://", "https://").trim();

            // Download the image associated with the article
            Glide.with(DrinkByIngredient.this)
                    .load(newUrl)
                    .into(imageViewDownload);
        }

    }

    //metodo che permette di visualizzare il drink
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void visualizzaDrink(int posizione){

        //Inizializzazione delle varie textbox con gli elementi associati al drink che vogliamo visualizzare.
        textView_Ingredienti_Drink.setText(recuperaIngredienti(posizione));
        textView_QuantitaIngredienti_Drink.setText(recuperaQuantitaIngredienti(posizione));
        textView_Nome_Drink.setText(drinksWithDrinksApi.get(posizione).getStrDrink());
        textView_Alchool_Drink.setText(drinksWithDrinksApi.get(posizione).getStrAlcoholic());
        textView_Preparazione_Drink.setText(drinksWithDrinksApi.get(posizione).getStrInstructionsIT());
        imgGlide(drinksWithDrinksApi.get(posizione).getStrDrinkThumb());
        cardView_InfoDrink.setVisibility(View.VISIBLE);
        //text_nome.setVisibility(View.VISIBLE);
        text_gradazione.setVisibility(View.VISIBLE);
        text_ingredienti.setVisibility(View.VISIBLE);
        text_quantita.setVisibility(View.VISIBLE);
        text_preparazione.setVisibility(View.VISIBLE);
        imageViewDownload.setVisibility(View.VISIBLE);

        //se stiamo visualizzando un drink che è già presente nel nostro elenco preferiti dovremo modificare il bottone_Salva
        boolean trovato=false;
        for(int i=0; i<drinksPreferiti.size();i++)
        {
            if(Integer.parseInt(drinksPreferiti.get(i))==drinksWithDrinksApi.get(posizione).getIdDrink()){
                trovato=true;
            }
        }

        if(trovato){
            setChangesButtonSalva();
        }

    }

    //Recupera la lista degli ingredienti associata ad un determinato drink passato per posizione
    private String recuperaIngredienti(int posizione){
        List<String> listaIngredienti = new ArrayList<>();
        String ingredienti = "";
        if(drinksWithDrinksApi.get(posizione).getStrIngredient1() != null){
            listaIngredienti.add(drinksWithDrinksApi.get(posizione).getStrIngredient1());
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient2() != null){
            listaIngredienti.add(drinksWithDrinksApi.get(posizione).getStrIngredient2());
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient3() != null){
            listaIngredienti.add(drinksWithDrinksApi.get(posizione).getStrIngredient3());
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient4() != null){
            listaIngredienti.add(drinksWithDrinksApi.get(posizione).getStrIngredient4());
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient5() != null){
            listaIngredienti.add(drinksWithDrinksApi.get(posizione).getStrIngredient5());
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient6() != null){
            listaIngredienti.add(drinksWithDrinksApi.get(posizione).getStrIngredient6());
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient7() != null){
            listaIngredienti.add(drinksWithDrinksApi.get(posizione).getStrIngredient7());
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient8() != null){
            listaIngredienti.add(drinksWithDrinksApi.get(posizione).getStrIngredient8());
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient9() != null){
            listaIngredienti.add(drinksWithDrinksApi.get(posizione).getStrIngredient9());
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient10() != null){
            listaIngredienti.add(drinksWithDrinksApi.get(posizione).getStrIngredient10());
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient11() != null){
            listaIngredienti.add(drinksWithDrinksApi.get(posizione).getStrIngredient11());
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient12() != null){
            listaIngredienti.add(drinksWithDrinksApi.get(posizione).getStrIngredient12());
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient13() != null){
            listaIngredienti.add(drinksWithDrinksApi.get(posizione).getStrIngredient13());
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient14() != null){
            listaIngredienti.add(drinksWithDrinksApi.get(posizione).getStrIngredient14());
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient15() != null){
            listaIngredienti.add(drinksWithDrinksApi.get(posizione).getStrIngredient15());
        }
        for(int i = 0; i<(listaIngredienti.size())-1;i++){
            ingredienti += listaIngredienti.get(i)+"\n";
        }
        ingredienti = ingredienti.concat(listaIngredienti.get(listaIngredienti.size()-1)+"");
        return ingredienti;
    }

    //Recupera la quantità degli ingredienti associata ad un determinato drink passato per posizione
    private String recuperaQuantitaIngredienti(int posizione){
        String quantita = "";
        List<String> listaQuantita = new ArrayList<>();
        if(drinksWithDrinksApi.get(posizione).getStrMeasure1() != null){
            listaQuantita.add(drinksWithDrinksApi.get(posizione).getStrMeasure1());
        }
        if(drinksWithDrinksApi.get(posizione).getStrMeasure2() != null){
            listaQuantita.add(drinksWithDrinksApi.get(posizione).getStrMeasure2());
        }
        if(drinksWithDrinksApi.get(posizione).getStrMeasure3() != null){
            listaQuantita.add(drinksWithDrinksApi.get(posizione).getStrMeasure3());
        }
        if(drinksWithDrinksApi.get(posizione).getStrMeasure4() != null){
            listaQuantita.add(drinksWithDrinksApi.get(posizione).getStrMeasure4());
        }
        if(drinksWithDrinksApi.get(posizione).getStrMeasure5() != null){
            listaQuantita.add(drinksWithDrinksApi.get(posizione).getStrMeasure5());
        }
        if(drinksWithDrinksApi.get(posizione).getStrMeasure6() != null){
            listaQuantita.add(drinksWithDrinksApi.get(posizione).getStrMeasure6());
        }
        if(drinksWithDrinksApi.get(posizione).getStrMeasure7() != null){
            listaQuantita.add(drinksWithDrinksApi.get(posizione).getStrMeasure7());
        }
        if(drinksWithDrinksApi.get(posizione).getStrMeasure8() != null){
            listaQuantita.add(drinksWithDrinksApi.get(posizione).getStrMeasure8());
        }
        if(drinksWithDrinksApi.get(posizione).getStrMeasure9() != null){
            listaQuantita.add(drinksWithDrinksApi.get(posizione).getStrMeasure9());
        }
        if(drinksWithDrinksApi.get(posizione).getStrMeasure10() != null){
            listaQuantita.add(drinksWithDrinksApi.get(posizione).getStrMeasure10());
        }
        if(drinksWithDrinksApi.get(posizione).getStrMeasure11() != null){
            listaQuantita.add(drinksWithDrinksApi.get(posizione).getStrMeasure11());
        }
        if(drinksWithDrinksApi.get(posizione).getStrMeasure12() != null){
            listaQuantita.add(drinksWithDrinksApi.get(posizione).getStrMeasure12());
        }
        if(drinksWithDrinksApi.get(posizione).getStrMeasure13() != null){
            listaQuantita.add(drinksWithDrinksApi.get(posizione).getStrMeasure13());
        }
        if(drinksWithDrinksApi.get(posizione).getStrMeasure14() != null){
            listaQuantita.add(drinksWithDrinksApi.get(posizione).getStrMeasure14());
        }
        if(drinksWithDrinksApi.get(posizione).getStrMeasure15() != null){
            listaQuantita.add(drinksWithDrinksApi.get(posizione).getStrMeasure15());
        }
        for(int i = 0; i<(listaQuantita.size())-1;i++){
            quantita += listaQuantita.get(i)+"\n";
        }
        quantita = quantita.concat(listaQuantita.get(listaQuantita.size()-1)+"");
        return quantita;
    }

    //Metodo che attiva o disattiva i bottoni a seconda delle esigenze
    public void attivaBottoni(){

        if (posizione==0 && drinksWithDrinksApi.size()==0)
        {
            button_Successivo_Drink.setVisibility(View.INVISIBLE);
            button_Precedente_Drink.setVisibility(View.INVISIBLE);

        }
        else if (posizione==0 && drinksWithDrinksApi.size()!=0)
        {
            button_Successivo_Drink.setVisibility(View.VISIBLE);
            button_Precedente_Drink.setVisibility(View.INVISIBLE);

        }
        else if (posizione == drinksWithDrinksApi.size()-1)
        {
            button_Successivo_Drink.setVisibility(View.INVISIBLE);
            button_Precedente_Drink.setVisibility(View.VISIBLE);

        }else
        {
            button_Successivo_Drink.setVisibility(View.VISIBLE);
            button_Precedente_Drink.setVisibility(View.VISIBLE);
        }

    }

    private void salvaIdDrink(int idDrink) throws IOException {
        //LibFileExt.writeFile("ElencoIdDrink", ""+idDrink);
        scriviFile(idDrink);
        //String contenuto = leggiFile(scriviFile(idDrink));
    }

    private File scriviFile(int data) throws IOException {
        File path = this.getFilesDir(); //==> data/data/com.example.drinkup/files
        String idDrink = ""+data+"\n";

        File file = new File(path, "ElencoPreferiti.txt");
        Log.d("testPath2", file.toString());
        if(!file.exists()){
            FileOutputStream stream = new FileOutputStream(file);
            try {
                stream.write(idDrink.getBytes());
            } finally {
                stream.close();
            }

        } else{
            FileOutputStream stream = new FileOutputStream(file, true);
            OutputStreamWriter outWriter = new OutputStreamWriter(stream);
            try {
                outWriter.append(idDrink);
            } finally {
                outWriter.close();
                stream.close();
            }
        }

        return file;

    }

    //metodo che legge il file e ritorna una stringa contenente i valori letti
    private String leggiFile(File file) throws IOException {
        int length = (int) file.length();

        byte[] bytes = new byte[length];

        FileInputStream in = new FileInputStream(file);
        try {
            in.read(bytes);
        } finally {
            in.close();
        }

        String contents = new String(bytes);

        return contents;
    }

    //metodo che permette di salvare in una lista l'elenco dei drink preferiti letti dal file, invoca il metodo esterno leggiFile
    private void RecuperaDrinkPreferiti() throws IOException {
        File path = this.getFilesDir(); //==> data/data/com.example.drinkup/files
        File file = new File(path, "ElencoPreferiti.txt");
        String stringElencoPreferiti = leggiFile(file);
        drinksPreferiti= Arrays.asList(stringElencoPreferiti.split("\n"));
    }

    //Metodo che permette di cancellare un drink tra i preferiti nel file locale
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void cancellaDrinkdaFile(int id) throws IOException {
        File path = this.getFilesDir(); //==> data/data/com.example.drinkup/files
        File file = new File(path, "ElencoPreferiti.txt");
        Integer value = new Integer(id);
        String daRimuovere = value.toString();
        //Toast.makeText(this, daRimuovere, Toast.LENGTH_LONG).show();

        //Parte nuova, al posto di lavorare sull'arrayList originale, se ne si fa una copia e si lavora su quella
        List<String> drinksPreferitiClone = new ArrayList<>();
        drinksPreferitiClone.addAll(drinksPreferiti);
        for(int i=0;i<drinksPreferitiClone.size();i++){
            String daRemove = drinksPreferitiClone.get(i);
            if(daRemove.equals(id+"")){
                drinksPreferitiClone.remove(daRemove);
                i--;
                //break;
            }
        }
        file.delete();
        for(int i=0;i<drinksPreferitiClone.size();i++){
            scriviFile(Integer.parseInt(drinksPreferitiClone.get(i)));
        }
        //Una volta fatto, riportiamo tutti i valori nell'elenco originale
        //Per farlo prima liberiamo la lista, con il clear crasha quindi si crea nuova
        drinksPreferiti = new ArrayList<>();
        drinksPreferiti.addAll(drinksPreferitiClone);
        setDefaultButtonSalva();
    }

    //metodo che permette di riportare i valori di default al bottone "Salva Preferito"
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setDefaultButtonSalva(){
        button_Salva_Preferito.setBackgroundColor(0xFFCA4700);
        button_Salva_Preferito.setForeground(null);
    }

    //metodo che permette cambiamenti grafici una volta premuto il bottone "Salva Preferito"
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setChangesButtonSalva(){
        button_Salva_Preferito.setBackgroundColor(0xFFDAA520);
        Drawable drawable = getResources().getDrawable(android.R.drawable.btn_star_big_on);
        button_Salva_Preferito.setForeground(drawable);
        button_Salva_Preferito.setForegroundGravity(View.TEXT_ALIGNMENT_GRAVITY);
    }

}