    <xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
        <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
        <xsl:strip-space elements="*"/>

        <!-- modified identity transform -->
        <xsl:template match="/domain_objects">
            <xsl:element name="{local-name()}">        
                <!-- <xsl:apply-templates select="gateway" /> -->
                <xsl:apply-templates select="appliance" />
                <!-- <xsl:apply-templates select="location" /> -->
                <!-- <xsl:apply-templates select="module" /> -->
            </xsl:element>
        </xsl:template>

        <xsl:template match="node()">
            <!-- prevent duplicate siblings -->
            <xsl:if test="count(preceding-sibling::node()[name()=name(current())])=0">
                <!-- copy element -->
                <xsl:copy>
                    <xsl:apply-templates select="@*|node()"/>
                </xsl:copy>
            </xsl:if>        
        </xsl:template>

        <xsl:template match="appliance">
            <xsl:copy/>
        </xsl:template>

        <!-- attributes to elements -->
        <xsl:template match="@*">
            <xsl:element name="{name()}">
                <xsl:value-of select="."/>
            </xsl:element>
        </xsl:template>

    </xsl:stylesheet>