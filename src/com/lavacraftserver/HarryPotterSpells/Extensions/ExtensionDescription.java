package com.lavacraftserver.HarryPotterSpells.Extensions;

/**
 * The description file for extensions
 */
public class ExtensionDescription {
	private String name, pack, version, description, author;

	/**
	 * Gets the name of the extension
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the main class of the extension
	 * @return the main class
	 */
	public String getPackage() {
		return pack;
	}

	/**
	 * Gets the version string of the extension
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Gets the description of the extension
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Gets the author of the extension
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}
	
	protected void setName(String name) {
		this.name = name;
	}

	protected void setPackage(String pack) {
		this.pack = pack;
	}

	protected void setVersion(String version) {
		this.version = version;
	}

	protected void setDescription(String description) {
		this.description = description;
	}

	protected void setAuthor(String author) {
		this.author = author;
	}
	
}
