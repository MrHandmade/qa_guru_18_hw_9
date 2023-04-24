import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileTest {
    public ClassLoader cl = FileTest.class.getClassLoader();


    @Test
    void zipPdfTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("Data.zip");
             ZipInputStream zs = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zs.getNextEntry()) != null) {
                if (entry.getName().equals("Sample text.pdf")) {
                    PDF pdf = new PDF(zs);
                    Assertions.assertEquals("Sample text", pdf.text.trim());
                }
            }
        }
    }

    @Test
    void zipExcelTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("Data.zip");
             ZipInputStream zs = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zs.getNextEntry()) != null) {
                if (entry.getName().equals("Data.xlsx")) {
                    XLS xls = new XLS(zs);
                    Assertions.assertEquals("ТЕСТ ТЕКСТ", xls.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue());
                }
            }
        }
    }

    @Test
    void zipCsvTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("Data.zip");
             ZipInputStream zs = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zs.getNextEntry()) != null) {
                if (entry.getName().equals("data.csv")) {
                    CSVReader csvReader = new CSVReader(new InputStreamReader(zs));
                    List<String[]> content = csvReader.readAll();
                    Assertions.assertArrayEquals(new String[]{"email", "password"}, content.get(0));
                }
            }
        }
    }

    @Test
    void jsonTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream is = cl.getResourceAsStream("player.json");
             InputStreamReader jsr = new InputStreamReader(is)) {
            Player player = objectMapper.readValue(jsr, Player.class);

            Assertions.assertEquals("Sam", player.nickname);
            Assertions.assertEquals("Human", player.fraction);
            Assertions.assertEquals("axe", player.weapons.get(0));
            Assertions.assertEquals("sword", player.weapons.get(1));
        }
    }

}


