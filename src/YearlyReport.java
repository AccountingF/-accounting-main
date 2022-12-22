import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class YearlyReport {
    HashMap<Integer, ArrayList<String[]>> yearlyInfo = new HashMap<>();
    ArrayList<String[]> yearly = new ArrayList<>();
    String path = "";
    String readingResult;
    String[] result;
    String[] resultValues;
    int yearDate;
    int wrongCounter = 0;
    HashMap<Integer, ArrayList<Integer>> monthsInfo = new HashMap<>();

    public YearlyReport(){}
    public YearlyReport(HashMap<Integer, ArrayList<Integer>> monthsInfo){
        this.monthsInfo = monthsInfo;
    }

    public void readFileYear(String newPath)
    {
        try{
            //File dir = new File("C:\\resources");
            File dir = new File(newPath);
            for(File item : Objects.requireNonNull(dir.listFiles())){
                path = dir + "\\" + item.getName();
                readingResult = readFileContentsOrNull(path);
                String firstLetter = String.valueOf(item.getName().charAt(0));

                if (firstLetter.equals("y")){
                    yearDate = Integer.parseInt(item.getName().substring(2, 6)); //Узнаем год файла
                    result = readingResult.split("\\n");

                    for (int i = 1; i < result.length; i++) {
                        resultValues = result[i].split(",");
                        yearly.add(resultValues);
                    }
                    yearlyInfo.put(yearDate, yearly);
                    yearly = new ArrayList<>();
                }
                else if (!firstLetter.equals("m"))
                    wrongCounter++;
            }
            //System.out.println("Годовой отчет был считан!");
            if (wrongCounter == 1)
                System.out.println("В вашей папке, находящейся по пути " + dir + " есть 1 файл с неправильным именем.");
                //System.out.println("There is 1 file with the wrong name in your folder located in the path " + dir + ".");
            else if (wrongCounter > 1)
                System.out.println("В вашей папке, находящейся по пути " + dir + " есть " + wrongCounter + " файлов с неправильным именем.");
                //System.out.println("There are " + dir + " files in your folder in the path " + wrongCounter + " with the wrong name.");
        } catch (NullPointerException e) {
            System.out.println("В данной директории отсутствуют файлы!");
            //System.out.println("There are no files in this directory!");
        }
    }
    public void yearOutput(){
        if (yearlyInfo.keySet().isEmpty())
            System.out.println("Информации о годовом отчете не было найдено.");
            //System.out.println("No information about the annual report was found.");

        int numOfProfit = 0;
        int numOfExpense = 0;
        int trueNumMonth = -1;
        int falseNumMonth = -2;
        int profit = 0;
        int profitCount = 0;
        int expense = 0;
        int expenseCount = 0;
        for (int numYear : yearlyInfo.keySet()) {
            yearly = yearlyInfo.get(numYear);
            //System.out.println(numYear);
            for (String[] numMassive : yearly) {
                if (numMassive[2].equals("false")){
                    falseNumMonth = Integer.parseInt(numMassive[0]);
                    numOfProfit = Integer.parseInt(numMassive[1]);
                    profit += numOfProfit;
                    profitCount ++;
                }
                else if (numMassive[2].equals("true")){
                    trueNumMonth = Integer.parseInt(numMassive[0]);
                    numOfExpense = Integer.parseInt(numMassive[1]);
                    expense += numOfExpense;
                    expenseCount ++;
                }
                //if (trueNumMonth == falseNumMonth){
                    //System.out.println("Прибыль за " + trueNumMonth + "-й месяц составила: " + (numOfProfit - numOfExpense));
                    //System.out.println("Profit for " + trueNumMonth + "th month was: " + (numOfProfit - numOfExpense));
                //}
            }
            /*System.out.println("Средний расход за все месяцы в году: " + ((double) expense / expenseCount));
            System.out.println("Средний доход за все месяцы в году: " + ((double) profit / profitCount));*/
            //System.out.println("Average expense for all months of the year: " + ((double) expense / expenseCount));
            //System.out.println("Average income for all months of the year: " + ((double) profit / profitCount));
            int emptyMonth = 1;
            for (int month : monthsInfo.keySet()) {
                while (emptyMonth != month && emptyMonth < 13) {
                    System.out.println(emptyMonth + "_0");
                    emptyMonth++;
                }
                System.out.println(month + "_" + monthsInfo.get(month).get(0));
                if (emptyMonth < 13) {
                    emptyMonth++;
                }
            }
            while (emptyMonth < 13) {
                System.out.println(emptyMonth + "_0");
                emptyMonth++;
            }
            System.out.println(numYear + "_" + profit); //!!!
        }
    }
    public void reportAnalysis() {
        if ((yearlyInfo.keySet().isEmpty()) && (monthsInfo.keySet().isEmpty())) {
            System.out.println("Информации о годовом и месячных отчетах не было найдено.");
            //System.out.println("Information on annual and monthly reports was not found.");
            return;
        }
        else if (yearlyInfo.keySet().isEmpty()) {
            System.out.println("Информации о годовом отчете не было найдено.");
            //System.out.println("Information about the annual report was not found.");
            return;
        }
        else if (monthsInfo.keySet().isEmpty()) {
            System.out.println("Информации о месячных отчетах не было найдено.");
            //System.out.println("Information about monthly reports was not found.");
            return;
        }
        int trueNumMonth = -1;
        int falseNumMonth = -2;
        int numOfProfit = 0;
        int numOfExpense = 0;
        int reportsCounter = 0;
        for (int numYear : yearlyInfo.keySet()) {
            yearly = yearlyInfo.get(numYear);
            for (String[] numMassive : yearly) {
                if (numMassive[2].equals("false")){
                    falseNumMonth = Integer.parseInt(numMassive[0]);
                    numOfProfit = Integer.parseInt(numMassive[1]);
                }
                else if (numMassive[2].equals("true")){
                    trueNumMonth = Integer.parseInt(numMassive[0]);
                    numOfExpense = Integer.parseInt(numMassive[1]);
                }
                if (trueNumMonth == falseNumMonth){
                    ArrayList<Integer> monthlyValue = monthsInfo.get(trueNumMonth);
                    if ((numOfProfit != monthlyValue.get(0)) || (numOfExpense != monthlyValue.get(1))){
                        System.out.println("Обнаружено несоответствие в месяце: " + numMonthString(Integer.parseInt(numMassive[0])));
                        //System.out.println("Mismatch detected in month: " + numMonthString(Integer.parseInt(numMassive[0])));
                        reportsCounter++;
                    }
                }
            }
        }
        if (reportsCounter == 0)
            System.out.println("Несоответствий обнаружено не было!");
            //System.out.println("No inconsistencies were found!");
    }
    private String readFileContentsOrNull(String path)
    {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл с месячным отчётом. Возможно, файл не находится в нужной директории.");
            //System.out.println("Unable to read monthly report file. The file may not be in the correct directory.");
            return null;
        }
    }
    public String numMonthString(int numMonth) {
        List<String> months = Arrays.asList("Январь","Февраль","Март","Апрель","Май","Июнь","Июль","Август","Сентябрь","Октябрь","Ноябрь","Декабрь");
        //List<String> months = Arrays.asList("January","February","March","April","May","June","July","August","September","October", "November", "December");
        if (numMonth > 0 && numMonth < 13)
            return months.get(numMonth-1);
        return null;
    }
}