package main.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Material;

final public class FinalFieldSetter {

	public static void main(String[] args) throws Exception {
		Material m = Material.DIAMOND_PICKAXE;
		Class<?> c = m.getClass();
		Field f = c.getDeclaredField("maxStack");
		f.setAccessible( true );
		getInstance().set(m, f, "10");
	}

    private static final FinalFieldSetter INSTANCE;

    static {
        try {
            INSTANCE = new FinalFieldSetter();
        } catch (final ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private final Object unsafeObj;

    private final Method putObjectMethod;

    private final Method objectFieldOffsetMethod;

    private final Method staticFieldOffsetMethod;

    private final Method staticFieldBaseMethod;

    private FinalFieldSetter() throws ReflectiveOperationException {

        final Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");

        final Field unsafeField = unsafeClass.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);

        this.unsafeObj = unsafeField.get(null);

        this.putObjectMethod = unsafeClass.getMethod("putObject", Object.class,
            long.class, Object.class);
        this.objectFieldOffsetMethod = unsafeClass.getMethod("objectFieldOffset",
            Field.class);
        this.staticFieldOffsetMethod = unsafeClass.getMethod("staticFieldOffset",
            Field.class);
        this.staticFieldBaseMethod = unsafeClass.getMethod("staticFieldBase",
            Field.class);
    }

    public static FinalFieldSetter getInstance() {
        return INSTANCE;
    }

    public void set(final Object o, final Field field, final Object value) throws Exception {

        final Object fieldBase = o;
        final long fieldOffset = (long) this.objectFieldOffsetMethod.invoke(
            this.unsafeObj, field);

        this.putObjectMethod.invoke(this.unsafeObj, fieldBase, fieldOffset, value);
    }

    public void setStatic(final Field field, final Object value) throws Exception {

        final Object fieldBase = this.staticFieldBaseMethod.invoke(this.unsafeObj, field);
        final long fieldOffset = (long) this.staticFieldOffsetMethod.invoke(
            this.unsafeObj, field);

        this.putObjectMethod.invoke(this.unsafeObj, fieldBase, fieldOffset, value);
    }
}