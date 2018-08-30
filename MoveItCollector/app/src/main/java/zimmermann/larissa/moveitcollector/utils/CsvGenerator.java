package zimmermann.larissa.moveitcollector.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import zimmermann.larissa.moveitcollector.database.Project;


public class CsvGenerator {
    private static final String TAG = CsvGenerator.class.getName();

    private Project mProject;
    private File mFolder;
    private List<File> mFileList;
    String filename;
    private File mCsvFile;

    public CsvGenerator(Context context, Project project) {
        mProject = project;
        mFolder = FileManager.buildResultPath(context);
        fillFileList(context);
        filename = project.getName().replace(" ", "") + "_" + project.getStartDate().getTime() + ".csv";
        mCsvFile = new File(mFolder, filename);
    }

    public static File generateCSVFileName(Context context, Project project) {
        File folder = FileManager.buildResultPath(context);
        String fileName = project.getName().replace(" ", "") + "_" + project.getStartDate().getTime() + ".csv";
        return new File(folder, fileName);
    }

    private void fillFileList(Context context) {
        mFileList = new ArrayList<File>();
        File[] files = FileManager.buildProjectPath(context, mProject.getProjectId()).listFiles();
        if(files != null) {
            for(File file : files) {
                mFileList.add(file);
            }
        }
    }

    public void generateCSV() throws IOException {
        if (!mCsvFile.getParentFile().exists()) {
            mCsvFile.getParentFile().mkdirs();
        }

        if(!mCsvFile.exists()) {
            mCsvFile.createNewFile();
        }

        //FileWriter writer = new FileWriter(mCsvFile.getName(), false);
        FileOutputStream mFileOutputStream = new FileOutputStream(mCsvFile, true);
        OutputStreamWriter mOutputStreamWriter = new OutputStreamWriter(mFileOutputStream);
        BufferedWriter mBufferedWriter = new BufferedWriter(mOutputStreamWriter);
        PrintWriter mPrintWriter = new PrintWriter(mBufferedWriter);

        FileInputStream fileInputStream;
        BufferedReader reader;


        CsvUtils.writeLine(mPrintWriter, new String[]{"Timestamp", "Ax", "Ay", "Az", "Acceleration", "ActivityLabel"});

        for(File file : mFileList) { //For each file
            //For each line
            if(!file.isDirectory() && file.exists()) {

                fileInputStream = new FileInputStream(file);
                reader = new BufferedReader(new InputStreamReader(fileInputStream));
                String line = reader.readLine();

                while(line != null){

                    String[] fields = line.split(",");

                    CsvUtils.writeLine(mPrintWriter, fields);

                    line = reader.readLine();
                }

            }
        }

        mPrintWriter.flush();
        mPrintWriter.close();
    }

}
