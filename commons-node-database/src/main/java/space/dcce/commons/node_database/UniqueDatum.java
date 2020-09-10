package space.dcce.commons.node_database;

import java.util.UUID;

import org.apache.commons.lang3.builder.HashCodeBuilder;


// TODO: Auto-generated Javadoc
/**
 * The Class UniqueDatum.
 */
public abstract class UniqueDatum
{
	
	/** The uuid. */
	private UUID uuid;
	
	/** The database. */
	protected final NodeDatabase database;

	/**
	 * Instantiates a new unique datum.
	 *
	 * @param database the database
	 */
	protected UniqueDatum(NodeDatabase database)
	{
		this.database = database;
		this.uuid = UUID.randomUUID();
	}


	/**
	 * Instantiates a new unique datum.
	 *
	 * @param database the database
	 * @param uuid the uuid
	 */
	protected UniqueDatum(NodeDatabase database, UUID uuid)
	{
		this.database = database;
		if (uuid == null)
		{
			this.uuid = UUID.randomUUID();
		}
		else
		{
			this.uuid = uuid;
		}
	}


	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public UUID getID()
	{
		return uuid;
	}
	


	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof UniqueDatum)
		{
			UniqueDatum o = (UniqueDatum) obj;
			return o.uuid.equals(uuid);
		}
		else
		{
			return false;
		}
	}


	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode()
	{
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(uuid);
		return hcb.toHashCode();
	}

}
