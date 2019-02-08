package borg.ed.galaxy.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MiscUtil
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public abstract class MiscUtil {

	static final Logger logger = LoggerFactory.getLogger(MiscUtil.class);

	public static float levenshteinError(CharSequence mostLikelyCorrectReferenceString, CharSequence someOtherString) {
		float dist = StringUtils.getLevenshteinDistance(mostLikelyCorrectReferenceString.toString().toLowerCase(), someOtherString.toString().toLowerCase());
		float err = dist / mostLikelyCorrectReferenceString.length();

		return err;
	}

	/*
	 * ==== ==== ==== ==== ==== ==== ==== ==== GENERICS ==== ==== ==== ==== ==== ==== ==== ====
	 */

	/**
	 * Unsafe casting of an unknown <code>Collection</code> to a typed <code>Collection</code>.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Collection<T> unsafeCast(Collection<?> sourceCollection) {
		return (Collection<T>) sourceCollection;
	}

	/**
	 * Unsafe casting of an unknown <code>List</code> to a typed <code>List</code>.
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> unsafeCast(List<?> sourceList) {
		return (List<T>) sourceList;
	}

	/**
	 * Unsafe casting of an unknown <code>Set</code> to a typed <code>Set</code>.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Set<T> unsafeCast(Set<?> sourceSet) {
		return (Set<T>) sourceSet;
	}

	/**
	 * Unsafe casting of an unknown <code>Map</code> to a typed <code>Map</code>.
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> unsafeCast(Map<?, ?> sourceMap) {
		return (Map<K, V>) sourceMap;
	}

	/**
	 * Unsafe casting of an unknown <code>Iterator</code> to a typed <code>Iterator</code>.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Iterator<T> unsafeCast(Iterator<?> sourceIterator) {
		return (Iterator<T>) sourceIterator;
	}

	/**
	 * Unsafe casting of an unknown <code>Enumeration</code> to a typed <code>Enumeration</code>.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Enumeration<T> unsafeCast(Enumeration<?> sourceEnum) {
		return (Enumeration<T>) sourceEnum;
	}

	public static String getAsString(Object o) {
		return MiscUtil.getAsString(o, null);
	}

	public static String getAsString(Object o, String defaultValue) {
		if (o == null) {
			return defaultValue;
		} else {
			String s = o.toString();

			if (StringUtils.isEmpty(s)) {
				return defaultValue;
			} else {
				return s;
			}
		}
	}

	public static boolean getAsBoolean(Object o) {
		return MiscUtil.getAsBoolean(o, false);
	}

	public static boolean getAsBoolean(Object o, Boolean defaultValue) {
		if (o != null) {
			if (o instanceof Boolean) {
				return (Boolean) o;
			} else {
				final String s = o.toString().toLowerCase();

				if (s.length() > 0) {
					return "true".equals(s) || "yes".equals(s) || "y".equals(s) || "on".equals(s) || "1".equals(s);
				}
			}
		}

		return Boolean.TRUE.equals(defaultValue);
	}

	public static Boolean getAsBooleanObject(Object o) {
		return MiscUtil.getAsBooleanObject(o, null);
	}

	public static Boolean getAsBooleanObject(Object o, Boolean defaultValue) {
		if (o != null) {
			if (o instanceof Boolean) {
				return (Boolean) o;
			} else {
				final String s = o.toString().toLowerCase();

				if (s.length() > 0) {
					return "true".equals(s) || "yes".equals(s) || "y".equals(s) || "on".equals(s) || "1".equals(s);
				}
			}
		}

		return defaultValue;
	}

	public static Number getAsNumber(Object o) {
		return MiscUtil.getAsNumber(o, null);
	}

	public static Number getAsNumber(Object o, Number defaultValue) {
		if (o == null) {
			return null;
		} else if (o instanceof Number) {
			return (Number) o;
		} else {
			BigDecimal bigDecimal = MiscUtil.getAsBigDecimal(o);

			return bigDecimal == null ? defaultValue : bigDecimal;
		}
	}

	public static Integer getAsInt(Object o) {
		return MiscUtil.getAsInteger(o); // Just an alias
	}

	public static Integer getAsInt(Object o, Integer defaultValue) {
		return MiscUtil.getAsInteger(o, defaultValue); // Just an alias
	}

	public static Integer getAsInteger(Object o) {
		return MiscUtil.getAsInteger(o, null);
	}

	public static Integer getAsInteger(Object o, Integer defaultValue) {
		if (o != null) {
			if (o instanceof Integer) {
				return (Integer) o;
			} else if (o instanceof Number) {
				return ((Number) o).intValue();
			} else {
				Number number = getAsNumber(o, defaultValue);

				if (number != null) {
					return number.intValue();
				}
			}
		}

		return defaultValue;
	}

	public static Long getAsLong(Object o) {
		return MiscUtil.getAsLong(o, null);
	}

	public static Long getAsLong(Object o, Long defaultValue) {
		if (o != null) {
			if (o instanceof Long) {
				return (Long) o;
			} else if (o instanceof Number) {
				return ((Number) o).longValue();
			} else {
				Number number = getAsNumber(o, defaultValue);

				if (number != null) {
					return number.longValue();
				}
			}
		}

		return defaultValue;
	}

	public static Float getAsFloat(Object o) {
		return MiscUtil.getAsFloat(o, null);
	}

	public static Float getAsFloat(Object o, Float defaultValue) {
		if (o != null) {
			if (o instanceof Float) {
				return (Float) o;
			} else if (o instanceof Number) {
				return ((Number) o).floatValue();
			} else {
				Number number = getAsNumber(o, defaultValue);

				if (number != null) {
					return number.floatValue();
				}
			}
		}

		return defaultValue;
	}

	public static Double getAsDouble(Object o) {
		return MiscUtil.getAsDouble(o, null);
	}

	public static Double getAsDouble(Object o, Double defaultValue) {
		if (o != null) {
			if (o instanceof Double) {
				return (Double) o;
			} else if (o instanceof Number) {
				return ((Number) o).doubleValue();
			} else {
				Number number = getAsNumber(o, defaultValue);

				if (number != null) {
					return number.doubleValue();
				}
			}
		}

		return defaultValue;
	}

	public static BigDecimal getAsBigDecimal(Object o) {
		return MiscUtil.getAsBigDecimal(o, null);
	}

	public static BigDecimal getAsBigDecimal(Object o, BigDecimal defaultValue) {
		if (o != null) {
			if (o instanceof BigDecimal) {
				return (BigDecimal) o;
			} else {
				final String s = o.toString().replace(',', '.');

				if (s.length() > 0) {
					try {
						return new BigDecimal(s);
					} catch (Exception e1) {
						logger.warn("Failed to parse '" + s + "' as BigDecimal");

						try {
							return new BigDecimal(s.replaceAll("[^\\d\\-\\.]", ""));
						} catch (Exception e2) {
							// Abort
						}
					}
				}
			}
		}

		return defaultValue;
	}

	public static void sortMapByValueReverse(LinkedHashMap map) {
		MiscUtil.sortMapByValue(map, null, true);
	}

	public static void sortMapByValue(LinkedHashMap map) {
		MiscUtil.sortMapByValue(map, null, false);
	}

	public static void sortMapByValue(LinkedHashMap map, Comparator comparator) {
		MiscUtil.sortMapByValue(map, null, false);
	}

	/**
	 * Sorts a map by its values. The map must be a <code>LinkedHashMap</code>, because only this type of map has a predictable iteration order.
	 *
	 * @param map
	 *            Map to be sorted
	 * @param comparator
	 *            Comparator for comparing the map's values or <code>null</code> for natural ordering of the values.
	 * @since 3.5
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void sortMapByValue(LinkedHashMap map, Comparator comparator, boolean reverse) {
		// Make two copies of the original map: A list of all keys and one of all values
		List mapKeys = new ArrayList(map.keySet());
		List mapValues = new ArrayList(map.values());

		// Sort all values
		List sortedValues = new ArrayList();
		sortedValues.addAll(mapValues);
		Collections.sort(sortedValues, comparator);
		if (reverse) {
			Collections.reverse(sortedValues);
		}

		// Clear the original map
		map.clear();

		// Re-fill the original map in a sorted order
		for (Object value : sortedValues) {
			int index = mapValues.indexOf(value); // Index within the original map
			Object key = mapKeys.get(index); // Key from the original map

			map.put(key, value); // Re-fill

			mapValues.remove(index); // Do not find this key/value again
			mapKeys.remove(index); // Do not find this key/value again
		}
	}

}
