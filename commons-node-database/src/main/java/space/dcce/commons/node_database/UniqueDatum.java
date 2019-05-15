package space.dcce.commons.node_database;

import java.util.UUID;

import org.apache.commons.lang3.builder.HashCodeBuilder;


public abstract class UniqueDatum
{
	private UUID uuid;
	protected final NodeDatabase database;

	protected UniqueDatum(NodeDatabase database)
	{
		this.database = database;
		this.uuid = UUID.randomUUID();
	}


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


	public UUID getID()
	{
		return uuid;
	}
	


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


	@Override
	public int hashCode()
	{
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(uuid);
		return hcb.toHashCode();
	}

}
