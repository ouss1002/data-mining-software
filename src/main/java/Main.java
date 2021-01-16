import tech.tablesaw.api.Table;
import tech.tablesaw.io.csv.CsvReadOptions;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        Table t = readData();

        System.out.println(t);
    }

    static Table readData() throws IOException {
        CsvReadOptions.Builder builder = CsvReadOptions.builder("src/main/resources/Thyroid_Dataset.txt").
                header(false).tableName("Thyroid Dataset");

        CsvReadOptions options = builder.build();
        Table t = Table.read().usingOptions(options);
        String[] column_names = {"class", "t3_resin", "total_thyroxin", "total_triio", "tsh", "max_diff_tsh"};
        for(int i=0;i<column_names.length;i++){
            t.column(i).setName(column_names[i]);
        }
        return t;
    }

}
