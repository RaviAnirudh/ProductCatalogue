package com.api.Helper;

import com.api.Entity.Product;
import com.api.Respository.ProductRepository;
import com.api.Service.ProductService;
import com.api.validator.FileValidator;
//import javafx.util.Pair;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

public class ProductHelper {
    @Autowired
    private static ProductService productService;

    public static boolean checkDocumentFormat(MultipartFile file) throws IOException{
        return file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
    public static List<Product> convertExcelToListOfProduct(InputStream is){
        List<Product> productList = new ArrayList<>();
        try{
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheet("Data");
            int rowNumber = 0;
            Iterator<Row> iter = sheet.iterator();
            while (iter.hasNext()){
                Row row = iter.next();
                if (rowNumber == 0){
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cells = row.iterator();
                int coloumnId = 0;
                Product product = new Product();
                while (cells.hasNext()){
                    Cell cell = cells.next();
                    switch (coloumnId){
                        case 0 :
                            product.setProductName(cell.getStringCellValue());
                            break;
                        case 1:
                            product.setProductCategory(cell.getStringCellValue());
                            break;
                        case 2:
                            product.setProductDescription(cell.getStringCellValue());
                            break;
                        case 3 :
                            product.setProductQuantity((long) cell.getNumericCellValue());
                            break;
                        case 4 :
                            product.setProductPrice(cell.getNumericCellValue());
                            break;
                        case 5 :
                            product.setMerchantId((long) cell.getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    coloumnId++;
                }
                productList.add(product);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return productList;
    }
    public static String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<Product> csvToProducts(InputStream is) {
        String message = "";
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());)  {
            List<Product> products = new ArrayList<Product>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            Integer count = 1;
            for (CSVRecord csvRecord : csvRecords) {
                Product product = new Product();
                if(FileValidator.checkMerchantId(Long.parseLong(csvRecord.get("MerchantId")))){
                    product.setMerchantId(Long.parseLong(csvRecord.get("MerchantId")));
                    product.setProductPrice(Double.parseDouble(csvRecord.get("Price")));
                    product.setProductName(csvRecord.get("Name"));
                    product.setProductDescription(csvRecord.get("Description"));
                    product.setProductCategory( csvRecord.get("Category"));
                    product.setProductQuantity( Long.parseLong(csvRecord.get("Quantity")));
                    message = message + count.toString()+"."+ FileValidator.validity(product);
                    products.add(product);
                }else continue;
            }
            System.out.println("\n Error in data : \n"+ message + " \n");
            return products;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}

