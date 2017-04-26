package net.dacce.commons.general;

import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;


public class CustomToStringStyle extends ToStringStyle
{
	private final static Logger logger = LoggerFactory.getLogger(CustomToStringStyle.class);


	public CustomToStringStyle()
	{
	}


//	public CustomToStringStyle(ToStringStyle target)
//	{
//		setArrayEnd(target.getArrayEnd());
//		setArraySeparator(target.getArraySeparator());
//		setArrayStart(target.getArrayStart());
//		setContentEnd(target.getContentEnd());
//		setContentStart(target.getContentStart());
//		setFieldNameValueSeparator(target.getFieldNameValueSeparator());
//		setFieldSeparator(target.getFieldSeparator());
//		setNullText(target.getNullText());
//		setSizeEndText(target.getSizeEndText());
//		setSizeStartText(target.getSizeStartText());
//		setSummaryObjectEndText(target.getSummaryObjectEndText());
//		setSummaryObjectStartText(target.getSummaryObjectStartText());
//		setArrayContentDetail(target.getArrayContentDetail());
//		setDefaultFullDetail(target.getDefaultFullDetail());
//		setFieldSeparatorAtEnd(target.getFieldSeparatorAtEnd());
//		setFieldSeparatorAtStart(target.getFieldSeparatorAtStart());
//		setUseClassName(target.getUseClassName());
//		setUseFieldNames(target.getUseFieldNames());
//		setUseIdentityHashCode(target.getUseIdentityHashCode());
//		setUseShortClassName(target.getUseShortClassName());
//	}


	@Override
	public void setUseClassName(boolean useClassName)
	{
		super.setUseClassName(useClassName);
	}


	@Override
	public void setUseShortClassName(boolean useShortClassName)
	{
		super.setUseShortClassName(useShortClassName);
	}


	@Override
	public void setUseIdentityHashCode(boolean useIdentityHashCode)
	{
		super.setUseIdentityHashCode(useIdentityHashCode);
	}


	@Override
	public void setUseFieldNames(boolean useFieldNames)
	{

		super.setUseFieldNames(useFieldNames);
	}


	@Override
	public void setDefaultFullDetail(boolean defaultFullDetail)
	{
		super.setDefaultFullDetail(defaultFullDetail);
	}


	@Override
	public void setArrayContentDetail(boolean arrayContentDetail)
	{
		super.setArrayContentDetail(arrayContentDetail);
	}


	@Override
	public void setArrayStart(String arrayStart)
	{
		super.setArrayStart(arrayStart);
	}


	@Override
	public void setArrayEnd(String arrayEnd)
	{
		super.setArrayEnd(arrayEnd);
	}


	@Override
	public void setArraySeparator(String arraySeparator)
	{
		super.setArraySeparator(arraySeparator);
	}


	@Override
	public void setContentStart(String contentStart)
	{
		super.setContentStart(contentStart);
	}


	@Override
	public void setContentEnd(String contentEnd)
	{
		super.setContentEnd(contentEnd);
	}


	@Override
	public void setFieldNameValueSeparator(String fieldNameValueSeparator)
	{
		super.setFieldNameValueSeparator(fieldNameValueSeparator);
	}


	@Override
	public void setFieldSeparator(String fieldSeparator)
	{
		super.setFieldSeparator(fieldSeparator);
	}


	@Override
	public void setFieldSeparatorAtStart(boolean fieldSeparatorAtStart)
	{
		super.setFieldSeparatorAtStart(fieldSeparatorAtStart);
	}


	@Override
	public void setFieldSeparatorAtEnd(boolean fieldSeparatorAtEnd)
	{
		super.setFieldSeparatorAtEnd(fieldSeparatorAtEnd);
	}


	@Override
	public void setNullText(String nullText)
	{
		super.setNullText(nullText);
	}


	@Override
	public void setSizeStartText(String sizeStartText)
	{
		super.setSizeStartText(sizeStartText);
	}


	@Override
	public void setSizeEndText(String sizeEndText)
	{
		super.setSizeEndText(sizeEndText);
	}


	@Override
	public void setSummaryObjectStartText(String summaryObjectStartText)
	{
		super.setSummaryObjectStartText(summaryObjectStartText);
	}


	@Override
	public void setSummaryObjectEndText(String summaryObjectEndText)
	{
		super.setSummaryObjectEndText(summaryObjectEndText);
	}
}
