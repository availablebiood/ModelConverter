package com.jogeen.converter.modelconverter;

import java.util.ArrayList;
import java.util.List;

import com.jogeen.converter.modelconverter.conn.DataConnectConfig;
import com.jogeen.converter.modelconverter.exception.HandlerException;
import com.jogeen.converter.modelconverter.extractor.IDataExtractor;
import com.jogeen.converter.modelconverter.extractor.impl.MysqlDataExtractor;
import com.jogeen.converter.modelconverter.factory.MysqlFieldTypeFactory;
import com.jogeen.converter.modelconverter.handle.IModelHandler;
import com.jogeen.converter.modelconverter.handle.java.JavaClassInfo;
import com.jogeen.converter.modelconverter.handle.java.JavaModelHandler;
import com.jogeen.converter.modelconverter.model.Column;
import com.jogeen.converter.modelconverter.model.OutputModel;
import com.jogeen.converter.modelconverter.model.TableModel;
import com.jogeen.converter.modelconverter.persistent.IPersistent;
import com.jogeen.converter.modelconverter.persistent.impl.file.JavaFilePersistent;

public class Main {
	public static void main(String[] args) {
		DataConnectConfig config = new DataConnectConfig("localhost", 3306, "root", "root", "ordersystem");
		IDataExtractor dataProcessor = new MysqlDataExtractor(config);
		List<String> tableNames = dataProcessor.getTableNames("plan_manage");
		List<OutputModel> list = new ArrayList<OutputModel>();
		for (String tableName : tableNames) {
			System.out.println(tableName);
			List<Column> columns = dataProcessor.getColumns("plan_manage", tableName);
			for (Column column : columns) {
				System.out.println(column.toString());
			}
			IModelHandler handle = new JavaModelHandler(MysqlFieldTypeFactory.getInstance().createFieldTypeMapping(),new JavaClassInfo());
			TableModel classModel = new TableModel();
			classModel.setColumns(columns);
			classModel.setTableName(tableName);
			try {
				OutputModel dealModel = handle.dealModel(classModel);
				list.add(dealModel);
				System.out.println(dealModel.getClassContent());
			} catch (HandlerException e) {
				e.printStackTrace();
			}
		}
		IPersistent persistent = new JavaFilePersistent("E:/work/");
		try {
			persistent.persistent(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<String> dataBaseNames = dataProcessor.getDataBaseNames();
		for (String string : dataBaseNames) {
			System.out.println(string);
		}
	}
}
