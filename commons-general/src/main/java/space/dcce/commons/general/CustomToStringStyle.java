package space.dcce.commons.general;

import org.apache.commons.lang3.builder.ToStringStyle;


// TODO: Auto-generated Javadoc
/**
 * The Class CustomToStringStyle.
 */
public class CustomToStringStyle extends ToStringStyle
{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Instantiates a new custom to string style.
	 */
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


	/**
 * Sets the use class name.
 *
 * @param useClassName the new use class name
 */
@Override
	public void setUseClassName(boolean useClassName)
	{
		super.setUseClassName(useClassName);
	}


	/**
	 * Sets the use short class name.
	 *
	 * @param useShortClassName the new use short class name
	 */
	@Override
	public void setUseShortClassName(boolean useShortClassName)
	{
		super.setUseShortClassName(useShortClassName);
	}


	/**
	 * Sets the use identity hash code.
	 *
	 * @param useIdentityHashCode the new use identity hash code
	 */
	@Override
	public void setUseIdentityHashCode(boolean useIdentityHashCode)
	{
		super.setUseIdentityHashCode(useIdentityHashCode);
	}


	/**
	 * Sets the use field names.
	 *
	 * @param useFieldNames the new use field names
	 */
	@Override
	public void setUseFieldNames(boolean useFieldNames)
	{

		super.setUseFieldNames(useFieldNames);
	}


	/**
	 * Sets the default full detail.
	 *
	 * @param defaultFullDetail the new default full detail
	 */
	@Override
	public void setDefaultFullDetail(boolean defaultFullDetail)
	{
		super.setDefaultFullDetail(defaultFullDetail);
	}


	/**
	 * Sets the array content detail.
	 *
	 * @param arrayContentDetail the new array content detail
	 */
	@Override
	public void setArrayContentDetail(boolean arrayContentDetail)
	{
		super.setArrayContentDetail(arrayContentDetail);
	}


	/**
	 * Sets the array start.
	 *
	 * @param arrayStart the new array start
	 */
	@Override
	public void setArrayStart(String arrayStart)
	{
		super.setArrayStart(arrayStart);
	}


	/**
	 * Sets the array end.
	 *
	 * @param arrayEnd the new array end
	 */
	@Override
	public void setArrayEnd(String arrayEnd)
	{
		super.setArrayEnd(arrayEnd);
	}


	/**
	 * Sets the array separator.
	 *
	 * @param arraySeparator the new array separator
	 */
	@Override
	public void setArraySeparator(String arraySeparator)
	{
		super.setArraySeparator(arraySeparator);
	}


	/**
	 * Sets the content start.
	 *
	 * @param contentStart the new content start
	 */
	@Override
	public void setContentStart(String contentStart)
	{
		super.setContentStart(contentStart);
	}


	/**
	 * Sets the content end.
	 *
	 * @param contentEnd the new content end
	 */
	@Override
	public void setContentEnd(String contentEnd)
	{
		super.setContentEnd(contentEnd);
	}


	/**
	 * Sets the field name value separator.
	 *
	 * @param fieldNameValueSeparator the new field name value separator
	 */
	@Override
	public void setFieldNameValueSeparator(String fieldNameValueSeparator)
	{
		super.setFieldNameValueSeparator(fieldNameValueSeparator);
	}


	/**
	 * Sets the field separator.
	 *
	 * @param fieldSeparator the new field separator
	 */
	@Override
	public void setFieldSeparator(String fieldSeparator)
	{
		super.setFieldSeparator(fieldSeparator);
	}


	/**
	 * Sets the field separator at start.
	 *
	 * @param fieldSeparatorAtStart the new field separator at start
	 */
	@Override
	public void setFieldSeparatorAtStart(boolean fieldSeparatorAtStart)
	{
		super.setFieldSeparatorAtStart(fieldSeparatorAtStart);
	}


	/**
	 * Sets the field separator at end.
	 *
	 * @param fieldSeparatorAtEnd the new field separator at end
	 */
	@Override
	public void setFieldSeparatorAtEnd(boolean fieldSeparatorAtEnd)
	{
		super.setFieldSeparatorAtEnd(fieldSeparatorAtEnd);
	}


	/**
	 * Sets the null text.
	 *
	 * @param nullText the new null text
	 */
	@Override
	public void setNullText(String nullText)
	{
		super.setNullText(nullText);
	}


	/**
	 * Sets the size start text.
	 *
	 * @param sizeStartText the new size start text
	 */
	@Override
	public void setSizeStartText(String sizeStartText)
	{
		super.setSizeStartText(sizeStartText);
	}


	/**
	 * Sets the size end text.
	 *
	 * @param sizeEndText the new size end text
	 */
	@Override
	public void setSizeEndText(String sizeEndText)
	{
		super.setSizeEndText(sizeEndText);
	}


	/**
	 * Sets the summary object start text.
	 *
	 * @param summaryObjectStartText the new summary object start text
	 */
	@Override
	public void setSummaryObjectStartText(String summaryObjectStartText)
	{
		super.setSummaryObjectStartText(summaryObjectStartText);
	}


	/**
	 * Sets the summary object end text.
	 *
	 * @param summaryObjectEndText the new summary object end text
	 */
	@Override
	public void setSummaryObjectEndText(String summaryObjectEndText)
	{
		super.setSummaryObjectEndText(summaryObjectEndText);
	}
}
