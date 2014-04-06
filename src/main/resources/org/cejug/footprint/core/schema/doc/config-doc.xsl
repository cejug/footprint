<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
	<xsl:template match="xsd:schema">
		<xsl:variable name="ns" select='@targetNamespace' />
		<xsl:variable name="element" select='xsd:element' />

		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
			<head>
				<link rel='stylesheet' type='text/css' href='doc/style.css' />
				<title>Footprint configuration file (config.xsd)</title>
			</head>
			<body>
				<div class="fullscreen">
					<div class='topPane'>
						<div class='topLeftPane'>
							<img src='doc/small_logo.png' class='logo' />
						</div>
						<div class='topCenterPane fontHeader'>
							<p>Footprint-Config documentation</p>
						</div>
					</div>
					<div class="introduction">
						<p>
							This document describes the tags of the Footprint
							configuration
							file. You can find the schema that models the configuration file
							in the source of this page, just right click
							on this page and
							select
							<em>View Page Source</em>
							. For more details about the
							Footprint Project,
							please
							<a href='http://footprint.dev.java.net/'>click here</a>
							.
						</p>
					</div>
					<div class="elementPane">
						<xsl:for-each select="$element">
							<div class="element">
								<xsl:value-of select="@name" />
							</div>
							<xsl:for-each select="//xsd:complexType">
								<div class="complexType">
									<xsl:value-of select="@name" />
									<div class="documentation">
										<xsl:value-of select="xsd:annotation" />
									</div>

								</div>
							</xsl:for-each>
						</xsl:for-each>

					</div>

				</div>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="xsd:schema/xsd:annotation">
		<hr />
		<p>ll</p>
		<xsl:apply-templates select="xsd:documentation" />
	</xsl:template>

	<xsl:template match="xsd:documentation">
		<p>
			aaaaaaa no ta tion
		</p>
	</xsl:template>

	<xsl:template match="xsd:element[@name]">
		<xsl:variable name="schema" select="ancestor::xsd:schema" />
		<h2>
			<xsl:value-of select="schema" />
		</h2>
	</xsl:template>
</xsl:stylesheet>
