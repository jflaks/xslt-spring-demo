<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xalan/java"
    xmlns:transformContext="jflaks.xslt.TransformHelper"
               extension-element-prefixes="transformContext" >
    <xsl:variable name="transformContext" select="java:jflaks.xslt.TransformHelper.new()" />
    
	<xsl:template match="/">
		<xsl:variable name="shelter_contents"><xsl:apply-templates /></xsl:variable>
		<xsl:variable name="grave_contents"><xsl:apply-templates mode="grave" /></xsl:variable>
		{"shelter" : [<xsl:call-template name="removeTrailingCommaAndSpace">
			<xsl:with-param name="string" select="$shelter_contents"/></xsl:call-template>],
		"grave" : [<xsl:call-template name="removeTrailingCommaAndSpace">
			<xsl:with-param name="string" select="$grave_contents"/></xsl:call-template>]}	
	</xsl:template>
	
	<xsl:template match="town/person">
		{"name": "<xsl:value-of select="@name"/>"},	
	</xsl:template>
	
	<xsl:template match="town/zombie" mode="grave">
		{"description" : "<xsl:value-of select="@description" />"},
	</xsl:template>
	
	<xsl:template match="building[descendant::person]"> 
		<xsl:if test="count(.//zombie) &lt; 3">
			<xsl:apply-templates select=".//person" mode="extractPerson" />
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="building[descendant::person]" mode="grave"> 
		<xsl:if test="count(.//zombie) &lt; 3">
			<xsl:apply-templates select=".//person" mode="extractPersonReject" />
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="removeTrailingCommaAndSpace">
		<xsl:param name="string" />		
		<xsl:variable name="normalized" select="normalize-space($string)" />
		<xsl:if test="string-length($normalized) > 0">
			<xsl:value-of select="substring($normalized,1,string-length($normalized) - 1)" />
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="person" mode="extractPersonReject">
		<xsl:variable name="stateTrooperCheck" select="transformContext:checkHealth($transformContext,@name,@greeting)" />
		<xsl:if test="$stateTrooperCheck != 'Fine'">
			{"description" : "<xsl:value-of select="$stateTrooperCheck" />"},
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="person" mode="extractPerson">
		<xsl:variable name="stateTrooperCheck" select="transformContext:checkHealth($transformContext,@name,@greeting)" />
		<xsl:choose>
		<xsl:when test="$stateTrooperCheck = 'Fine'">
			{"name" : "<xsl:value-of select="@name" />" },
		</xsl:when>
		<xsl:otherwise>
			<xsl:message terminate="no"><xsl:value-of select="$stateTrooperCheck" /></xsl:message>
		</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>