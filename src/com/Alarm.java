package com;

public class Alarm {
	private String aid,email,lid;
	private int smokeLevel,co2Level,isActive,isWorking;
	
	public Alarm() {
		super();
	}
	
	public Alarm(String aid, String email, String lid, int smokeLevel, int co2Level, int isActive, int isWorking) {
		super();
		this.aid = aid;
		this.email = email;
		this.lid = lid;
		this.smokeLevel = smokeLevel;
		this.co2Level = co2Level;
		this.isActive = isActive;
		this.isWorking = isWorking;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLid() {
		return lid;
	}

	public void setLid(String lid) {
		this.lid = lid;
	}

	public int getSmokeLevel() {
		return smokeLevel;
	}

	public void setSmokeLevel(int smokeLevel) {
		this.smokeLevel = smokeLevel;
	}

	public int getCo2Level() {
		return co2Level;
	}

	public void setCo2Level(int co2Level) {
		this.co2Level = co2Level;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public int getIsWorking() {
		return isWorking;
	}

	public void setIsWorking(int isWorking) {
		this.isWorking = isWorking;
	}

	@Override
	public String toString() {
		return "Alarm [aid=" + aid + ", email=" + email + ", lid=" + lid + ", smokeLevel=" + smokeLevel + ", co2Level="
				+ co2Level + ", isActive=" + isActive + ", isWorking=" + isWorking + "]";
	}
	
}
