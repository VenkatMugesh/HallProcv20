package com.example.venkatmugesh.hallprocv20;

import android.Manifest;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ImportDetails extends AppCompatActivity {

    private static final String TAG = "ImportDetails";

    Button btnBack , btnForward;
    ListView internalListView;
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    File file;
    RegisterDB myDb;

    ArrayList<String> pathHistory;
    ArrayList<Upload> uploadData;
    String lastDirectory;
    int count = 0;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_details);

        btnBack = findViewById(R.id.btnBack);
        btnForward = findViewById(R.id.btnMemeory);
        internalListView = findViewById(R.id.contentListView);
        uploadData = new ArrayList<>();
        myDb = new RegisterDB(this);

        checkFilePermission();

        internalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lastDirectory = pathHistory.get(count);

                if(lastDirectory.equals(adapterView.getItemAtPosition(i))){
                    Log.i(TAG, "lvInternalStorage: Selected a file for upload: " + lastDirectory);
                    //Execute method for reading the excel data.
                    readExcelData(lastDirectory);
                    }else {
                    count++;
                    pathHistory.add(count,(String) adapterView.getItemAtPosition(i));
                    checkInternalStorage();
                    Log.i(TAG, "lvInternalStorage: " + pathHistory.get(count));

                }

            }

        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count == 0){
                    Log.d(TAG, "btnUpDirectory: You have reached the highest level directory.");
                    }else{
                    pathHistory.remove(count);
                    count--;
                    checkInternalStorage();
                    Log.d(TAG, "btnUpDirectory: " + pathHistory.get(count));
                    }

            }
        });

        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                pathHistory = new ArrayList<String>();
                pathHistory.add(count,System.getenv("EXTERNAL_STORAGE"));
                Log.d(TAG, "btnSDCard: " + pathHistory.get(count));
                checkInternalStorage();
            }
        });
    }

    private void readExcelData(String filePath) {

        Log.d(TAG, "readExcelData: Reading Excel File.");
        //decarle input file
        File inputFile = new File(filePath);

        try {
            InputStream inputStream = new FileInputStream(inputFile);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            Log.d(TAG , "value" + rowsCount);

            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            StringBuilder sb = new StringBuilder();
            //outter loop, loops through rows

            for (int r = 1; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();
                //inner loop, loops through columns
                for (int c = 0; c < cellsCount; c++) {
                    //handles if there are to many columns on the excel sheet.
                    if(c>2){
                        Log.e(TAG, "readExcelData: ERROR. Excel File Format is incorrect! " );
                        toastMessage("ERROR: Excel File Format is incorrect!");
                        break;

                    }else{
                        String value = getCellAsString(row, c, formulaEvaluator);
                        String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;
                        Log.d(TAG, "readExcelData: Data from row: " + cellInfo);
                        sb.append(value + ", ");

                    }

                }

                sb.append(":");

            }
            Log.d(TAG, "readExcelData: STRINGBUILDER: " + sb.toString());
            parseStringBuilder(sb);

        }catch (FileNotFoundException e) {
            Log.e(TAG, "readExcelData: FileNotFoundException. " + e.getMessage() );

        } catch (IOException e) {
            Log.e(TAG, "readExcelData: Error reading inputstream. " + e.getMessage() );

        }

    }

    public void parseStringBuilder(StringBuilder mStringBuilder){
        Log.d(TAG, "parseStringBuilder: Started parsing.");

        // splits the sb into rows.

        String[] rows = mStringBuilder.toString().split(":");
        //Add to the ArrayList<XYValue> row by row

        for(int i=0; i<rows.length; i++) {

            //Split the columns of the rows
            String[] columns = rows[i].split(",");
            //use try catch to make sure there are no "" that try to parse into doubles.
            try{
                double x =  Double.parseDouble(columns[0]);
                double y =  Double.parseDouble(columns[1]);
                double z =  Double.parseDouble(columns[2]);


                //add the the uploadData ArrayList
                uploadData.add(new Upload(x,y,z));

                }catch (NumberFormatException e){
                Log.e(TAG, "parseStringBuilder: NumberFormatException: " + e.getMessage());

                }
        }
        printDataToLog();
    }

    private void printDataToLog() {
        Log.d(TAG, "printDataToLog: Printing data to log...");
        for(int i = 0; i< uploadData.size(); i++){

            double x =  uploadData.get(i).getX();
            double y =  uploadData.get(i).getY();
            double z =  uploadData.get(i).getZ();
            boolean result = myDb.addData(x, y ,z);
            if(result){
                toastMessage("Added Successfully");
            }
            Log.d(TAG, "printDataToLog: (x,y,z): (" + x + "," + y + "," + z + ")");
        }
    }

    private String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {

        String value = "";

        try {

            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {

                case Cell.CELL_TYPE_BOOLEAN:

                    value = ""+cellValue.getBooleanValue();
                    break;

                case Cell.CELL_TYPE_NUMERIC:

                    double numericValue = cellValue.getNumberValue();
                    if(HSSFDateUtil.isCellDateFormatted(cell)) {

                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("MM/dd/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                        }
                        else {

                        value = ""+numericValue;
                    }
                    break;

                case Cell.CELL_TYPE_STRING:

                    value = ""+cellValue.getStringValue();
                    break;

                default:

            }

        } catch (NullPointerException e) {

            Log.e(TAG, "getCellAsString: NullPointerException: " + e.getMessage() );
        }

        return value;
    }

    private void checkInternalStorage() {

        Log.d(TAG, "checkInternalStorage: Started.");

        try{

            if (!Environment.getExternalStorageState().equals(

                    Environment.MEDIA_MOUNTED)) {

                toastMessage("No SD card found.");
            }

            else{

                // Locate the image folder in your SD Car;d

                file = new File(pathHistory.get(count));

                Log.d(TAG, "checkInternalStorage: directory path: " + pathHistory.get(count));

            }
            listFile = file.listFiles();

            // Create a String array for FilePathStrings

            FilePathStrings = new String[listFile.length];

            // Create a String array for FileNameStrings

            FileNameStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {

                // Get the path of the image file

                FilePathStrings[i] = listFile[i].getAbsolutePath();

                // Get the name image file
                FileNameStrings[i] = listFile[i].getName();
            }

            for (int i = 0; i < listFile.length; i++)

            {
                Log.d("Files", "FileName:" + listFile[i].getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FilePathStrings);
            internalListView.setAdapter(adapter);

        }catch(NullPointerException e){

            Log.e(TAG, "checkInternalStorage: NULLPOINTEREXCEPTION " + e.getMessage() );
        }

    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkFilePermission() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){

            int permissionCheck = this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");

            permissionCheck += this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");

            if (permissionCheck != 0) {



                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number

            }

        }else{

            Log.i("ImPort Details", "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");

        }
    }

    private void toastMessage(String message){

        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();

    }


}
