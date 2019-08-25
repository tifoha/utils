package net.tifoha.utils.io;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static net.tifoha.utils.io.IoUtils.close;

/**
 * @author Vitalii Sereda
 */
public interface SerializationHelper {
	/**
	 * Clone {@link Serializable} <T> {@link Collection}.
	 *
	 * @param toClone {@link Collection} with {@link Serializable} <T> to clone, can't be <code>null</code>
	 * @return {@link Collection} of <T>
	 */
	default <T extends Serializable> Collection<T> clone(Collection<T> toClone) {
		if (toClone == null)
			throw new IllegalArgumentException("toClone argument is null.");

		final List<T> result = new ArrayList<>();
		for (final T obj : toClone)
			result.add(obj != null ? clone(obj) : null);

		return result;
	}

	/**
	 * Clone {@link Serializable} <T>.
	 *
	 * @param toClone {@link Serializable} <T> to clone, can't be <code>null</code>
	 * @return <T>
	 */

	default <T extends Serializable> T clone(T toClone) {
		if (toClone == null)
			throw new IllegalArgumentException("toClone argument is null.");

		try {
			@SuppressWarnings("unchecked") final T result = (T) deserialize(serialize(toClone));
			return result;
		} catch (final ClassCastException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Serialize object to <code>byte</code> array.
	 *
	 * @param data {@link Serializable} object data, can't be <code>null</code>
	 * @return <code>byte</code> array
	 */
	default byte[] serialize(Serializable data) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream(getBufferSize());
		serialize(data, baos);
		return baos.toByteArray();
	}

	int getBufferSize();

	void serialize(Serializable data, OutputStream output);

	/**
	 * Deserialize object from <code>byte</code> array.
	 *
	 * @param rawData <code>byte</code> array, can't be <code>null</code>
	 * @return {@link Object}
	 */
	default Object deserialize(byte[] rawData) {
		if (rawData == null)
			throw new IllegalArgumentException("rawData argument is null");

		final ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
		return deserialize(bais);
	}

	/**
	 * Serialize object to {@link OutputStream} and close stream.
	 *
	 * @param data   {@link Serializable}, can't be <code>null</code>
	 * @param output {@link OutputStream}, can't be <code>null</code>
	 */
	default void serializeAndClose(Serializable data, OutputStream output) {
		try {
			serialize(data, output);
		} finally {
			close(output);
		}
	}

	Object deserialize(InputStream input);

	/**
	 * Deserialize object from {@link InputStream} and close stream.
	 *
	 * @param input {@link InputStream}, can't be <code>null</code>
	 * @return {@link Object}
	 */
	default Object deserializeAndClose(InputStream input) {
		try {
			return deserialize(input);
		} finally {
			close(input);
		}
	}

	Serializer getSerializer(OutputStream out);

	Deserializer getDeserializer(InputStream in);

	interface Serializer extends AutoCloseable {
		void flush();

		void serialize(Serializable obj);

		void serialize(long l);
	}

	interface Deserializer extends AutoCloseable {
		<T> T deserialize(Class<T> type);

		long deserializeLong();

		LongStream longStream();

        <T> Stream<T> objectStream(Class<T> type);
    }
}
