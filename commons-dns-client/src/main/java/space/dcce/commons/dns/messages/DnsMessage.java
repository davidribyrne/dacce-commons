/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */

package space.dcce.commons.dns.messages;


import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import space.dcce.commons.dns.records.ResourceRecord;


// TODO: Auto-generated Javadoc
/**
 * All communications inside of the domain protocol are carried in a single
 * format called a message.  The top level format of message is divided
 * into 5 sections (some of which are empty in certain cases) shown below:
 *
 *     +---------------------+
 *     |        Header       |
 *     +---------------------+
 *     |       Question      | the question for the name server
 *     +---------------------+
 *     |        Answer       | ResourceRecords answering the question
 *     +---------------------+
 *     |      Authority      | ResourceRecords pointing toward an authority
 *     +---------------------+
 *     |      Additional     | ResourceRecords holding additional information
 *     +---------------------+
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class DnsMessage
{
    /**
     * The header section is always present.  The header includes fields that
     * specify which of the remaining sections are present, and also specify
     * whether the message is a query or a response, a standard query or some
     * other opcode, etc.
     */
    private int transactionId;
    
    /** The message type. */
    private MessageType messageType;
    
    /** The op code. */
    private OpCode opCode;
    
    /** The authoritative answer. */
    private boolean authoritativeAnswer;
    
    /** The truncated. */
    private boolean truncated;
    
    /** The recursion desired. */
    private boolean recursionDesired;
    
    /** The recursion available. */
    private boolean recursionAvailable;
    
    /** The reserved. */
    private boolean reserved;
    
    /** The accept non authenticated data. */
    private boolean acceptNonAuthenticatedData;

    /** The response code. */
    private ResponseCode responseCode;

    /** The question records. */
    private List<QuestionRecord> questionRecords;
    
    /** The answer records. */
    private List<ResourceRecord> answerRecords;
    
    /** The authority records. */
    private List<ResourceRecord> authorityRecords;
    
    /** The additional records. */
    private List<ResourceRecord> additionalRecords;


    /**
     * Creates a new instance of DnsMessage.
     *
     * @param transactionId the transaction id
     * @param messageType the message type
     * @param opCode the op code
     * @param authoritativeAnswer the authoritative answer
     * @param truncated the truncated
     * @param recursionDesired the recursion desired
     * @param recursionAvailable the recursion available
     * @param reserved the reserved
     * @param acceptNonAuthenticatedData the accept non authenticated data
     * @param responseCode the response code
     * @param question the question
     * @param answer the answer
     * @param authority the authority
     * @param additional the additional
     */
    public DnsMessage( int transactionId, MessageType messageType, OpCode opCode, boolean authoritativeAnswer,
        boolean truncated, boolean recursionDesired, boolean recursionAvailable, boolean reserved,
        boolean acceptNonAuthenticatedData, ResponseCode responseCode, List<QuestionRecord> question,
        List<ResourceRecord> answer, List<ResourceRecord> authority, List<ResourceRecord> additional )
    {
        this.transactionId = transactionId;
        this.messageType = messageType;
        this.opCode = opCode;
        this.authoritativeAnswer = authoritativeAnswer;
        this.truncated = truncated;
        this.recursionDesired = recursionDesired;
        this.recursionAvailable = recursionAvailable;
        this.reserved = reserved;
        this.acceptNonAuthenticatedData = acceptNonAuthenticatedData;
        this.responseCode = responseCode;
        this.questionRecords = question;
        this.answerRecords = answer;
        this.authorityRecords = authority;
        this.additionalRecords = additional;
    }

    /**
     * Instantiates a new dns message.
     */
    public DnsMessage()
    {
    	
    }

    /**
     * Checks if is accept non authenticated data.
     *
     * @return Returns the acceptNonAuthenticatedData.
     */
    public boolean isAcceptNonAuthenticatedData()
    {
        return acceptNonAuthenticatedData;
    }


    /**
     * Gets the additional records.
     *
     * @return Returns the additional.
     */
    public List<ResourceRecord> getAdditionalRecords()
    {
        return additionalRecords;
    }


    /**
     * Gets the answer records.
     *
     * @return Returns the answers.
     */
    public List<ResourceRecord> getAnswerRecords()
    {
        return answerRecords;
    }


    /**
     * Checks if is authoritative answer.
     *
     * @return Returns the authoritativeAnswer.
     */
    public boolean isAuthoritativeAnswer()
    {
        return authoritativeAnswer;
    }


    /**
     * Gets the authority records.
     *
     * @return Returns the authority.
     */
    public List<ResourceRecord> getAuthorityRecords()
    {
        return authorityRecords;
    }


    /**
     * Gets the message type.
     *
     * @return Returns the messageType.
     */
    public MessageType getMessageType()
    {
        return messageType;
    }


    /**
     * Gets the op code.
     *
     * @return Returns the opCode.
     */
    public OpCode getOpCode()
    {
        return opCode;
    }


    /**
     * Gets the question records.
     *
     * @return Returns the question.
     */
    public List<QuestionRecord> getQuestionRecords()
    {
        return questionRecords;
    }


    /**
     * Checks if is recursion available.
     *
     * @return Returns the recursionAvailable.
     */
    public boolean isRecursionAvailable()
    {
        return recursionAvailable;
    }


    /**
     * Checks if is recursion desired.
     *
     * @return Returns the recursionDesired.
     */
    public boolean isRecursionDesired()
    {
        return recursionDesired;
    }


    /**
     * Checks if is reserved.
     *
     * @return Returns the reserved.
     */
    public boolean isReserved()
    {
        return reserved;
    }


    /**
     * Gets the response code.
     *
     * @return Returns the responseCode.
     */
    public ResponseCode getResponseCode()
    {
        return responseCode;
    }


    /**
     * Gets the transaction id.
     *
     * @return Returns the transactionId.
     */
    public int getTransactionId()
    {
        return transactionId;
    }


    /**
     * Checks if is truncated.
     *
     * @return Returns the truncated.
     */
    public boolean isTruncated()
    {
        return truncated;
    }


    /**
     * Equals.
     *
     * @param object the object
     * @return true, if successful
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals( Object object )
    {
        if ( object == this )
        {
            return true;
        }
        if ( !( object instanceof DnsMessage ) )
        {
            return false;
        }
        DnsMessage rhs = ( DnsMessage ) object;
        return new EqualsBuilder().append( this.transactionId, rhs.transactionId ).append( this.answerRecords,
            rhs.answerRecords ).append( this.opCode, rhs.opCode ).append( this.recursionAvailable,
            rhs.recursionAvailable ).append( this.messageType, rhs.messageType ).append( this.additionalRecords,
            rhs.additionalRecords ).append( this.truncated, rhs.truncated ).append( this.recursionDesired,
            rhs.recursionDesired ).append( this.responseCode, rhs.responseCode ).append( this.authorityRecords,
            rhs.authorityRecords ).append( this.authoritativeAnswer, rhs.authoritativeAnswer ).append( this.reserved,
            rhs.reserved ).append( this.acceptNonAuthenticatedData, rhs.acceptNonAuthenticatedData ).append(
            this.questionRecords, rhs.questionRecords ).isEquals();
    }


    /**
     * Hash code.
     *
     * @return the instance's hash code
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return new HashCodeBuilder( -1805208585, -276770303 ).append( this.transactionId ).append( this.answerRecords )
            .append( this.opCode ).append( this.recursionAvailable ).append( this.messageType ).append(
                this.additionalRecords ).append( this.truncated ).append( this.recursionDesired ).append(
                this.responseCode ).append( this.authorityRecords ).append( this.authoritativeAnswer ).append(
                this.reserved ).append( this.acceptNonAuthenticatedData ).append( this.questionRecords ).toHashCode();
    }


    /**
     * To string.
     *
     * @return the string
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return new ToStringBuilder( this ).appendSuper( super.toString() ).append( "transactionId", this.transactionId )
            .append( "opCode", this.opCode ).append( "truncated", this.truncated ).append( "messageType",
                this.messageType ).append( "recursionDesired", this.recursionDesired ).append( "additionalRecords",
                this.additionalRecords ).append( "responseCode", this.responseCode ).append( "authorityRecords",
                this.authorityRecords ).append( "acceptNonAuthenticatedData", this.acceptNonAuthenticatedData ).append(
                "recursionAvailable", this.recursionAvailable ).append( "answerRecords", this.answerRecords ).append(
                "questionRecords", this.questionRecords ).append( "authoritativeAnswer", this.authoritativeAnswer )
            .append( "reserved", this.reserved ).toString();
    }

	/**
	 * Sets the transaction id.
	 *
	 * @param transactionId the new transaction id
	 */
	public void setTransactionId(int transactionId)
	{
		this.transactionId = transactionId;
	}

	/**
	 * Sets the message type.
	 *
	 * @param messageType the new message type
	 */
	public void setMessageType(MessageType messageType)
	{
		this.messageType = messageType;
	}

	/**
	 * Sets the op code.
	 *
	 * @param opCode the new op code
	 */
	public void setOpCode(OpCode opCode)
	{
		this.opCode = opCode;
	}

	/**
	 * Sets the authoritative answer.
	 *
	 * @param authoritativeAnswer the new authoritative answer
	 */
	public void setAuthoritativeAnswer(boolean authoritativeAnswer)
	{
		this.authoritativeAnswer = authoritativeAnswer;
	}

	/**
	 * Sets the truncated.
	 *
	 * @param truncated the new truncated
	 */
	public void setTruncated(boolean truncated)
	{
		this.truncated = truncated;
	}

	/**
	 * Sets the recursion desired.
	 *
	 * @param recursionDesired the new recursion desired
	 */
	public void setRecursionDesired(boolean recursionDesired)
	{
		this.recursionDesired = recursionDesired;
	}

	/**
	 * Sets the recursion available.
	 *
	 * @param recursionAvailable the new recursion available
	 */
	public void setRecursionAvailable(boolean recursionAvailable)
	{
		this.recursionAvailable = recursionAvailable;
	}

	/**
	 * Sets the reserved.
	 *
	 * @param reserved the new reserved
	 */
	public void setReserved(boolean reserved)
	{
		this.reserved = reserved;
	}

	/**
	 * Sets the accept non authenticated data.
	 *
	 * @param acceptNonAuthenticatedData the new accept non authenticated data
	 */
	public void setAcceptNonAuthenticatedData(boolean acceptNonAuthenticatedData)
	{
		this.acceptNonAuthenticatedData = acceptNonAuthenticatedData;
	}

	/**
	 * Sets the response code.
	 *
	 * @param responseCode the new response code
	 */
	public void setResponseCode(ResponseCode responseCode)
	{
		this.responseCode = responseCode;
	}

	/**
	 * Sets the question records.
	 *
	 * @param questionRecords the new question records
	 */
	public void setQuestionRecords(List<QuestionRecord> questionRecords)
	{
		this.questionRecords = questionRecords;
	}

	/**
	 * Sets the answer records.
	 *
	 * @param answerRecords the new answer records
	 */
	public void setAnswerRecords(List<ResourceRecord> answerRecords)
	{
		this.answerRecords = answerRecords;
	}

	/**
	 * Sets the authority records.
	 *
	 * @param authorityRecords the new authority records
	 */
	public void setAuthorityRecords(List<ResourceRecord> authorityRecords)
	{
		this.authorityRecords = authorityRecords;
	}

	/**
	 * Sets the additional records.
	 *
	 * @param additionalRecords the new additional records
	 */
	public void setAdditionalRecords(List<ResourceRecord> additionalRecords)
	{
		this.additionalRecords = additionalRecords;
	}
}
