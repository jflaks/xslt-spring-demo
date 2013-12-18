<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
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
	
	<xsl:template name="removeTrailingCommaAndSpace">
		<xsl:param name="string" />		
		<xsl:variable name="normalized" select="normalize-space($string)" />
		<xsl:if test="string-length($normalized) > 0">
			<xsl:value-of select="substring($normalized,1,string-length($normalized) - 1)" />
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="person" mode="extractPerson">
		{name : <xsl:value-of select="@name" />},
	</xsl:template>
</xsl:stylesheet>