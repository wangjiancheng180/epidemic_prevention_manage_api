package com.wjc.excel.converter.university;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;

/**
 * @author 王建成
 * @date 2022/5/19--9:26
 */
public class VaccinationConverter implements Converter<Integer> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Integer convertToJavaData(ReadConverterContext<?> context) throws Exception {
        String cellStr = context.getReadCellData().getStringValue();
        if (StrUtil.isEmpty(cellStr)){
            return null;
        }
        if (cellStr.equals("未接种")){
            return 0;
        }else if (cellStr.equals("已接种1针")){
            return 1;
        }else if (cellStr.equals("完成全部接种")){
            return 2;
        }else if (cellStr.equals("完成加强针")){
            return 3;
        }
        return null;
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<Integer> context) throws Exception {
        Integer cellValue = context.getValue();
        switch (cellValue){
            case 0 :
                return new WriteCellData<>("未接种");
            case 1:
                return new WriteCellData<>("已接种1针");
            case 2:
                return new WriteCellData<>("完成全部接种");
            case 3:
                return new WriteCellData<>("完成加强针");
            default:
                return null;
        }
    }
}
