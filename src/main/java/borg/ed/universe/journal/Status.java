package borg.ed.universe.journal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Status implements Serializable {

	private static final long serialVersionUID = 3630837262232933689L;

	private ZonedDateTime timestamp = null;

	private String event = null;

	private int Flags = 0;

	private List<Integer> Pips = null; // in half pips, i.e. 2, 8, 2 = 1 SYS, 4 ENG, 1 WEP

	private int FireGroup = 0;

	private int GuiFocus = 0;

	private FuelStatus Fuel = null;

	private BigDecimal Cargo = null; // t

	private BigDecimal Latitude = null;

	private BigDecimal Longitude = null;

	private BigDecimal Altitude = null;

	private BigDecimal Heading = null;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Status other = (Status) obj;
		if (this.timestamp == null) {
			if (other.timestamp != null) {
				return false;
			}
		} else if (!this.timestamp.equals(other.timestamp)) {
			return false;
		}
		if (this.event == null) {
			if (other.event != null) {
				return false;
			}
		} else if (!this.event.equals(other.event)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.timestamp == null) ? 0 : this.timestamp.hashCode());
		result = prime * result + ((this.event == null) ? 0 : this.event.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return this.timestamp.format(DateTimeFormatter.ISO_INSTANT) + ": " + this.event + " (" + this.Flags + ")";
	}

	/**
	 * On a landing pad
	 */
	public boolean isDocked() {
		return testBit(this.Flags, 0);
	}

	/**
	 * On a planet
	 */
	public boolean isLanded() {
		return testBit(this.Flags, 1);
	}

	public boolean isLandingGearDown() {
		return testBit(this.Flags, 2);
	}

	public boolean isShieldsUp() {
		return testBit(this.Flags, 3);
	}

	public boolean isInSupercruise() {
		return testBit(this.Flags, 4);
	}

	public boolean isFlightAssistOff() {
		return testBit(this.Flags, 5);
	}

	public boolean isHardpointsDeployed() {
		return testBit(this.Flags, 6);
	}

	public boolean isInWing() {
		return testBit(this.Flags, 7);
	}

	public boolean isLightsOn() {
		return testBit(this.Flags, 8);
	}

	public boolean isCargoScoopDeployed() {
		return testBit(this.Flags, 9);
	}

	public boolean isSilentRunning() {
		return testBit(this.Flags, 10);
	}

	public boolean isScoopingFuel() {
		return testBit(this.Flags, 11);
	}

	public boolean isSrvHandbrake() {
		return testBit(this.Flags, 12);
	}

	public boolean isSrvTurret() {
		return testBit(this.Flags, 13);
	}

	public boolean isSrvUnderShip() {
		return testBit(this.Flags, 14);
	}

	public boolean isSrvDriveAssistOn() {
		return testBit(this.Flags, 15);
	}

	public boolean isFsdMassLocked() {
		return testBit(this.Flags, 16);
	}

	public boolean isFsdCharging() {
		return testBit(this.Flags, 17);
	}

	public boolean isFsdCooldown() {
		return testBit(this.Flags, 18);
	}

	/**
	 * Fuel &lt; 25%
	 */
	public boolean isLowFuel() {
		return testBit(this.Flags, 19);
	}

	/**
	 * Heat &gt; 100%
	 */
	public boolean isOverHeating() {
		return testBit(this.Flags, 20);
	}

	public boolean hasLatLon() {
		return testBit(this.Flags, 21);
	}

	public boolean isInDanger() {
		return testBit(this.Flags, 22);
	}

	public boolean isBeingInterdicted() {
		return testBit(this.Flags, 23);
	}

	public boolean isInMothership() {
		return testBit(this.Flags, 24);
	}

	public boolean isInFighter() {
		return testBit(this.Flags, 25);
	}

	public boolean isInSrv() {
		return testBit(this.Flags, 26);
	}

	public boolean isInAnalysisMode() {
		return testBit(this.Flags, 27);
	}

	public boolean isNightVisionOn() {
		return testBit(this.Flags, 28);
	}

	//    public boolean guiFocusNone() {
	//        return testBit(this.GuiFocus, 0);
	//    }

	public boolean guiFocusRightPanel() {
		return testBit(this.GuiFocus, 1);
	}

	public boolean guiFocusLeftPanel() {
		return testBit(this.GuiFocus, 2);
	}

	public boolean guiFocusCommsPanel() {
		return testBit(this.GuiFocus, 3);
	}

	public boolean guiFocusBottomPanel() {
		return testBit(this.GuiFocus, 4);
	}

	public boolean guiFocusStationServices() {
		return testBit(this.GuiFocus, 5);
	}

	public boolean guiFocusGalaxyMap() {
		return testBit(this.GuiFocus, 6);
	}

	public boolean guiFocusSystemMap() {
		return testBit(this.GuiFocus, 7);
	}

	public boolean guiFocusOrrery() {
		return testBit(this.GuiFocus, 8);
	}

	public boolean guiFocusFss() {
		return testBit(this.GuiFocus, 9);
	}

	public boolean guiFocusDss() {
		return testBit(this.GuiFocus, 10);
	}

	public boolean guiFocusCodex() {
		return testBit(this.GuiFocus, 11);
	}

	private static boolean testBit(int n, int pos) {
		return (n & 1 << pos) != 0;
	}

	@Getter
	@Setter
	public static class FuelStatus implements Serializable {

		private static final long serialVersionUID = 4922079506944916946L;

		private BigDecimal FuelMain = null; // t

		private BigDecimal FuelReservoir = null; // t

	}

}
