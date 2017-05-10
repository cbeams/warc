package org.iokit.core.config;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectorSpec {

    static class Foo {
        boolean b = false;
    }

    static class L1Holder {
        private Foo foo = new Foo();
    }

    static class L2Holder {
        L1Holder holder = new L1Holder();
    }

    static class L2HolderSubclass extends L2Holder {
    }

    private Reflector reflector = new Reflector();

    @Test
    public void testL1() throws Exception {

        L1Holder holder = new L1Holder();

        reflector.configure(holder, Foo.class, foo -> foo.b = true);

        assertThat(holder.foo.b).isTrue();
    }

    @Test
    public void testL2() throws Exception {

        L2Holder holder = new L2Holder();

        reflector.configure(holder, Foo.class, foo -> foo.b = true);

        assertThat(holder.holder.foo.b).isTrue();
    }

    @Test
    public void testL2Subclass() throws Exception {

        L2HolderSubclass holder = new L2HolderSubclass();

        reflector.configure(holder, Foo.class, foo -> foo.b = true);

        assertThat(holder.holder.foo.b).isTrue();
    }
}
