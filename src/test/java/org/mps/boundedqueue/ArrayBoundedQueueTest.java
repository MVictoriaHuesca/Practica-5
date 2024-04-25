package org.mps.boundedqueue;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

import java.util.Iterator;

public class ArrayBoundedQueueTest {

    @Nested
    @DisplayName("Clase para probar el constructor")
    public class ConstructorTest {
        @Test
        @DisplayName("Test que prueba que se crea una cola vacía correctamente")
        public void ArrayBoundedQueue_ColaVacia(){
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(2);

            assertThat(queue)
            .isNotNull();

            assertThat(queue.isEmpty())
            .isTrue();

            assertThat(queue.getLast())
            .isEqualTo(0);

            assertThat(queue.getFirst())
            .isEqualTo(0);
        }

        @Test
        @DisplayName("Test que prueba que lanza error cuando se crea una cola con capacidad negativa")
        public void ArrayBoundedQueue_CapacidadNegativa_ReturnException(){
            assertThatThrownBy(() -> new ArrayBoundedQueue<>(-2))
                                    .isInstanceOf(IllegalArgumentException.class)
                                    .hasMessage("ArrayBoundedException: capacity must be positive");
        }
        
    }

    @Nested
    @DisplayName("Clase para probar el método put")
    public class putTest {
        @Test
        @DisplayName("Prueba que se añade un elemento en la primera posicion de la cola correctamente")
        public void put_SeAñadeCorrectamenteEnLaPrimeraPosicion(){
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(3);

            queue.put(23);

            assertThat(queue)
            .hasSize(1)
            .containsExactly(23);

            assertThat(queue.getFirst())
            .isEqualTo(0);

            assertThat(queue.size())
            .isEqualTo(1);

            assertThat(queue.getLast())
            .isEqualTo(1);
        }
        @Test
        @DisplayName("Prueba que se añade un elemento a la cola correctamente")
        public void put_SeAñadeCorrectamenteEnLaPosicionNoUltimaNiPrimera(){
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(3);

            queue.put(23);
            queue.put(40);

            assertThat(queue)
            .hasSize(2)
            .containsExactly(23,40);

            assertThat(queue.getFirst())
            .isEqualTo(0);

            assertThat(queue.size()).isEqualTo(2);

            assertThat(queue.getLast()).isEqualTo(2);
        }

        @Test
        @DisplayName("Prueba que se añade un elemento en la ultima posicion de la cola correctamente")
        public void put_SeAñadeCorrectamenteEnLaUltimaPosicion(){
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(3);

            queue.put(23);
            queue.put(40);
            queue.put(55);


            assertThat(queue)
            .containsExactly(23,40,55);

            assertThat(queue.getFirst())
            .isEqualTo(0);

            assertThat(queue.size()).isEqualTo(3);

            assertThat(queue.getLast()).isEqualTo(0);
        }

        @Test
        @DisplayName("Prueba que lanza error cuando se añade un elemento a la cola llena")
        public void put_LaColaEstaLlena_ReturnException(){
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(2);

            queue.put(23);
            queue.put(40);

            assertThatThrownBy(() -> queue.put(55))
                                    .isInstanceOf(FullBoundedQueueException.class)
                                    .hasMessage("put: full bounded queue");
        }

        @Test
        @DisplayName("Prueba que lanza error cuando se añade un elemento nulo a la cola")
        public void put_ElementoNulo_ReturnException(){
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(2);

            assertThatThrownBy(() -> queue.put(null))
                                    .isInstanceOf(IllegalArgumentException.class)
                                    .hasMessage("put: element cannot be null");
        }
    }

    @Nested
    @DisplayName("Clase para probar el método get")
    public class getTest {
        @Test
        @DisplayName("Prueba que se elimina el ultimo elemento de la cola no llena correctamente")
        public void get_SeEliminaCorrectamenteCuandoLaColaNoEstaLlena(){
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(3);

            queue.put(23);
            queue.put(40);

            assertThat(queue.get())
            .isEqualTo(23);

            assertThat(queue)
            .hasSize(1);

            assertThat(queue.getFirst())
            .isEqualTo(1);

            assertThat(queue.getLast())
            .isEqualTo(2);
        }

        @Test
        @DisplayName("Prueba que se elimina el ultimo elemento de la cola llena correctamente")
        public void get_SeEliminaCorrectamenteCuandoLaColaEstaLlena(){
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(2);

            queue.put(23);
            queue.put(40);

            assertThat(queue.get())
            .isEqualTo(23);

            assertThat(queue.size())
            .isEqualTo(1);

            assertThat(queue.getFirst())
            .isEqualTo(1);

            assertThat(queue.getLast())
            .isEqualTo(0);
        }

        @Test
        @DisplayName("Prueba que lanza error cuando se elimina un elemento de la cola vacía")
        public void get_LaColaEstaVacia_ReturnException(){
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(2);

            assertThatThrownBy(() -> queue.get())
                                    .isInstanceOf(EmptyBoundedQueueException.class)
                                    .hasMessage("get: empty bounded queue");
        }
    }

    @Nested
    @DisplayName("Clase para probar el iterador")
    public class IteratorTest {
        @Test
        @DisplayName("Prueba que se recorre la cola correctamente")
        public void iterator_SeRecorreCorrectamente(){
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(3);
            queue.put(23);
            queue.put(40);
            queue.put(55);

            Iterator<Integer> it = queue.iterator();

            int aux=it.next();

            assertThat(aux).isEqualTo(23);
        }
    }



    


}
