package com.telusko.quizapp.service;

import com.telusko.quizapp.dao.QuestionDao;
import com.telusko.quizapp.model.Question;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import jakarta.persistence.Column;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import net.sf.jasperreports.export.XlsExporterConfiguration;
import net.sf.jasperreports.view.JasperViewer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;
    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
        	return new ResponseEntity<>(questionDao.findAll(),HttpStatus.OK);
        }catch(Exception e) {
        	e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
        
    }
    
	public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
		try {
        	return new ResponseEntity<>(questionDao.findByCategory(category),HttpStatus.OK);
        }catch(Exception e) {
        	e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
	}
	
	public ResponseEntity<String> addQuestion(Question question) {
		questionDao.save(question);
		return new ResponseEntity<>("Success",HttpStatus.CREATED);
	}
	
	public String dynamicReportBuilder() throws ClassNotFoundException, JRException {

	    //  FETCHING ALL RECORDS OF EMPOLYEE FROM DB
	    List<Question> allEmployeeList = questionDao.findAll();

	    FastReportBuilder drb = new FastReportBuilder();

//	    DynamicReport dr = drb.addColumn("ID", "id", Long.class.getName(),10)
//	            .addColumn("QuestionTitle", "questionTitle", String.class.getName(),30)
//	            .addColumn("category", "category", String.class.getName(),50)
//	            .addColumn("rightAnswer", "rightAnswer", String.class.getName(),50)
//	            .addColumn("difficultylevel", "difficultylevel", String.class.getName(),50,true)
//	            .addGroups(2)
//	            .setTitle("August 2021, Employee Detail Report")
//	            .setSubtitle("This report was generated at " + new Date())
//	            .setPrintBackgroundOnOddRows(true)
//	            .setUseFullPageWidth(true)
//	            .build();
	    
	    drb=printClassParameters(Question.class);
	    DynamicReport dr=drb.build();

	    JRDataSource ds = new JRBeanCollectionDataSource( allEmployeeList );
	    JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), ds);
//	    SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
//	    configuration.setOnePagePerSheet(true);
//	    configuration.setIgnoreGraphics(false);
//
//	    File outputFile = new File("output.xlsx");
//	    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//	         OutputStream fileOutputStream = new FileOutputStream(outputFile)) {
//	        Exporter exporter = new JRXlsxExporter();
//	        exporter.setExporterInput(new SimpleExporterInput(jp));
//	        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
//	        exporter.setConfiguration(configuration);
//	        exporter.exportReport();
//	        byteArrayOutputStream.writeTo(fileOutputStream);
//	    // PRIVIDING JAVA MODEL AS DATA SOURCE
	    
	    JasperViewer.viewReport(jp);
	    // FINALLY PRINTING THE REPORT

	    return "Report Geenrated";
//	    }catch(Exception e) {
//	    	e.printStackTrace();
//	    	return "Error while generating report";
//	    }
	}
	 public static String convertCamelToReadable(String camelCase) {
	        String regex = "([a-z])([A-Z]+)";
	        String replacement = "$1 $2";
	        Pattern pattern = Pattern.compile(regex);
	        Matcher matcher = pattern.matcher(camelCase);
	        String spacedString = matcher.replaceAll(replacement);

	        // Capitalize the first letter
	        return Character.toUpperCase(spacedString.charAt(0)) + spacedString.substring(1);
	    }
	 public static FastReportBuilder printClassParameters(Class<?> clazz) throws ColumnBuilderException, ClassNotFoundException {
		 	FastReportBuilder drb = new FastReportBuilder();
	        System.out.println("Class parameters for: " + clazz.getSimpleName());
	        Field[] fields = clazz.getDeclaredFields();
	        String columnName =null;
	        for (Field field : fields) {
	        	Class<?> fieldType = field.getType();
             String fieldName = field.getName();
	            if (field.isAnnotationPresent(Column.class)) {
	                Column columnAnnotation = field.getAnnotation(Column.class);
	                columnName = columnAnnotation.name();
	            }else {
	            	columnName=convertCamelToReadable(fieldName);
	            }
//	            System.out.println("Parameter: " + fieldName +
//                     ", Data Type: " + fieldType.getSimpleName() +
//                     ", Column Name: " + columnName);
	            drb.addColumn(columnName,fieldName,fieldType.getName(),50);
	        }
	        return drb;
	 }
}
