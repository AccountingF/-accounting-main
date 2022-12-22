import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Objects;

public class MonthlyReport {
    HashMap<Integer, ArrayList<String[]>> monthlyInfo = new HashMap<>();
    private final HashMap<Integer, ArrayList<Integer>> monthReporter = new HashMap<>();
    ArrayList<String[]> monthly = new ArrayList<>();
    String path = "";
    String readingResult;
    String[] result;
    String[] resultValues;
    int monthDate;
    int wrongCounter = 0;
    YearlyReport year = new YearlyReport(monthReporter);
    public void readFileMonths(String newPath)
    {
        try {
            //File dir = new File("C:\\resources");
            File dir = new File(newPath);
            for(File item : Objects.requireNonNull(dir.listFiles())){
                path = dir + "\\" + item.getName();
                readingResult = readFileContentsOrNull(path);
                String firstLetter = String.valueOf(item.getName().charAt(0));

                if (firstLetter.equals("m")) {
                    monthDate = Integer.parseInt(item.getName().substring(6, 8)); //Узнаем номер месяца файла
                    result = readingResult.split("\\n");

                    for (int i = 1; i < result.length; i++) {
                        resultValues = result[i].split(",");
                        monthly.add(resultValues);
                    }
                    monthlyInfo.put(monthDate, monthly);
                    monthly = new ArrayList<>();
                    monthReport(monthDate);
                }
                else if (!firstLetter.equals("y"))
                    wrongCounter++;
            }
            //System.out.println("Месячные отчеты были считаны!");
            if (wrongCounter == 1)
                System.out.println("В вашей папке, находящейся по пути " + dir + " есть 1 файл с неправильным именем.");
                //System.out.println("There is 1 file with the wrong name in your folder in the path " + dir + ".");
            else if (wrongCounter > 1)
                System.out.println("В вашей папке, находящейся по пути " + dir + " есть " + wrongCounter + " файлов с неправильным именем.");
                //System.out.println("There are " + dir + " files in your folder in the path " + wrongCounter + " with the wrong name.");
        } catch (NullPointerException e) {
            System.out.println("В данной директории отсутствуют файлы!");
            //System.out.println("There are no files in this directory!");
        }
    }
    public void monthsOutput(){
        if (monthlyInfo.keySet().isEmpty())
            System.out.println("Информации о месячных отчетах не было найдено.");
            //System.out.println("No monthly report information found.");
        //int emptyMonth = 1;
        for (int numMonth : monthlyInfo.keySet()) {
            int maxProfit = -1;
            int maxExpense = -1;
            String nameMaxProfit = "ТОВАР НЕ БЫЛ НАЙДЕН";
            String nameMaxExpense = "ТОВАР НЕ БЫЛ НАЙДЕН";
            //String nameMaxProfit = "THE PRODUCT WAS NOT FOUND";
            //String nameMaxExpense = "THE PRODUCT WAS NOT FOUND";
            monthly = monthlyInfo.get(numMonth);
            /*while (emptyMonth != numMonth && emptyMonth < 13) {
                System.out.println(year.numMonthString(emptyMonth));
                emptyMonth++;
            }
            if (emptyMonth < 13) {
                emptyMonth++;
            }*/
            System.out.println(year.numMonthString(numMonth));
            for (String[] numMassive : monthly) {
                if (numMassive[1].equals("FALSE")){
                    int num = Integer.parseInt(numMassive[2]) * Integer.parseInt(numMassive[3]);
                    if (num > maxProfit){
                        maxProfit = num;
                        nameMaxProfit = numMassive[0];
                    }
                }
                else if (numMassive[1].equals("TRUE")){
                    int num = Integer.parseInt(numMassive[2]) * Integer.parseInt(numMassive[3]);
                    if (num > maxExpense) {
                        maxExpense = num;
                        nameMaxExpense = numMassive[0];
                    }
                }
            }
            System.out.println("Самый прибыльный товар: " + nameMaxProfit + ". Его прибыль составила: " + maxProfit);
            System.out.println("Самая большая трата на товар: " + nameMaxExpense + ". Размер траты на него составил: " + maxExpense);
            //System.out.println("Most profitable product: " + nameMaxProfit + ". Its profit was: " + maxProfit);
            //System.out.println("The biggest spend on a product: " + nameMaxExpense + ". The amount spent on it was: " + maxExpense);
        }
        /*while (emptyMonth < 13) {
            System.out.println(year.numMonthString(emptyMonth));
            emptyMonth++;
        }*/
    }
    private void monthReport(Integer month){
        int profitSum = 0;
        int expenseSum = 0;
        ArrayList<Integer> result = new ArrayList<>();
        ArrayList<String[]> monthlyFirst = monthlyInfo.get(month);

        for (String[] numMassive : monthlyFirst) {
            if (numMassive[1].equals("TRUE")){
                expenseSum += (Integer.parseInt(numMassive[2]) * Integer.parseInt(numMassive[3]));
            }
            else if (numMassive[1].equals("FALSE")){
                profitSum += (Integer.parseInt(numMassive[2]) * Integer.parseInt(numMassive[3]));
            }
        }
        result.add(profitSum);
        result.add(expenseSum);
        monthReporter.put(month, result);
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
}