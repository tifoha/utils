package net.tifoha.utils.io;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.nustaq.serialization.FSTObjectSerializer;

import java.io.*;
import java.util.*;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Vitalii Sereda
 */
@Getter
public class FstSerializationHelper implements SerializationHelper {
	private static final int DEFAULT_BUFFER_SIZE = 512;
	/**
	 * Serialization buffer size.
	 */
	public final int bufferSize;
	private final FSTConfiguration conf;

	public FstSerializationHelper(int bufferSize, Class... frequentlyUsedClasses) {
		this.bufferSize = bufferSize;
		conf = FSTConfiguration.createDefaultConfiguration();
		conf.registerClass(frequentlyUsedClasses);
	}

	public FstSerializationHelper registerSerializer(Class type, FSTObjectSerializer serializer) {
		conf.registerSerializer(type, serializer, false);
		return this;
	}

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

		try {
			FSTObjectOutput out = conf.getObjectOutput(output);
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
			FSTObjectInput in = conf.getObjectInput();
			return in.readObject();
		} catch (final ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (final IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public Serializer getSerializer(OutputStream out) {
		return new FstSerializer(conf.getObjectOutput(out));
	}

	@Override
	public Deserializer getDeserializer(InputStream in) {
		return new FstDeserializer(conf.getObjectInput(in));
	}

	@Slf4j
	@AllArgsConstructor
	private static class FstSerializer implements Serializer {
		private final FSTObjectOutput delegate;

		@Override
		public void flush() {
			try {
				delegate.flush();
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}

		@Override
		public void serialize(Serializable obj) {
			try {
				delegate.writeObject(obj);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}

		@Override
		public void serialize(long l) {
			try {
				delegate.writeLong(l);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}

		@Override
		public void close() throws IOException {
			delegate.close();
		}
	}

	@Slf4j
	@AllArgsConstructor
	private static class FstDeserializer implements Deserializer {
		private final FSTObjectInput delegate;


		@Override
		public void close() throws Exception {
			delegate.close();
		}

		@Override
		@SuppressWarnings("unchecked")
		public <T> T deserialize(Class<T> type) {
			try {
				return (T) delegate.readObject(type);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public long deserializeLong() {
			try {
				return delegate.readLong();
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}

		@Override
		public LongStream longStream() {
			Spliterator.OfLong ofLong = Spliterators.spliterator(new LongIterator(delegate), 0, 0);
			return StreamSupport.longStream(ofLong, false);
		}

		@Override
		public <T> Stream<T> objectStream(Class<T> type) {
			Spliterator<T> spliterator = Spliterators.spliterator(new ObjectIterator(delegate, type), 0, 0);
			return StreamSupport.stream(spliterator, false);
		}
	}

	@AllArgsConstructor
	public static class LongIterator implements PrimitiveIterator.OfLong {
		private final FSTObjectInput input;

		@Override
		public boolean hasNext() {
			try {
				return input.available() > 0;
			} catch (IOException e) {
				return false;
			}
		}

		@Override
		public long nextLong() {
			if (hasNext()) {
				try {
					return input.readLong();
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			} else {
				throw new NoSuchElementException();
			}
		}
	}

	@AllArgsConstructor
	public static class ObjectIterator<T> implements Iterator<T> {
		private final FSTObjectInput input;
		private final Class type;

		@Override
		public boolean hasNext() {
			try {
				return input.available() > 0;
			} catch (IOException e) {
				return false;
			}
		}

		@Override
		public T next() {
			if (hasNext()) {
				try {
					return (T) input.readObject(type);
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			} else {
				throw new NoSuchElementException();
			}
		}
	}
}
