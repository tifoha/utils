package net.tifoha.utils.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.*;

@Getter
@Builder
@AllArgsConstructor
public class StandardSerializationHelper implements SerializationHelper {

	private static final int DEFAULT_BUFFER_SIZE = 512;
	/**
	 * Serialization buffer size.
	 */
	public final int bufferSize;

	/**
	 * Serialize object to {@link OutputStream}.
	 * leave output opened
	 *
	 * @param data   {@link Serializable}, can't be <code>null</code>
	 * @param output {@link OutputStream}, can't be <code>null</code>
	 */
	@Override
	public void serialize(final Serializable data, final OutputStream output) {
		if (data == null)
			throw new IllegalArgumentException("data argument is null");
		if (output == null)
			throw new IllegalArgumentException("output argument is null");

		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(output);
			out.writeObject(data);
			out.flush();
		} catch (final IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * Deserialize object from {@link InputStream}.
	 * leave input opened
	 *
	 * @param input {@link InputStream}, can't be <code>null</code>
	 * @return {@link Object}
	 */
	@Override
	public Object deserialize(final InputStream input) {
		if (input == null)
			throw new IllegalArgumentException("input argument is null");

		try {
			ObjectInputStream in = new ObjectInputStream(input);
			return in.readObject();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Serializer getSerializer(OutputStream out) {
		return null;
	}

	@Override
	public Deserializer getDeserializer(InputStream in) {
		return null;
	}

}
