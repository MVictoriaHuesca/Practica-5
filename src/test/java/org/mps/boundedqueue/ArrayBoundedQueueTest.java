/*
 * @autor1 = Eduardo García Rivas
 * @autor2 = María Victoria Huesca Peláez
 */

package org.mps.boundedqueue;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayBoundedQueueTest {

    @Nested
    @DisplayName("Clase para probar el constructor")
    public class ConstructorTest {
        @Test
        @DisplayName("Test que prueba que se crea una cola vacía correctamente")
        public void ArrayBoundedQueue_ColaSeCreaCorrectamente(){
            int capacidad = 2;
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(capacidad);
            int first = 0;
            int nextFree = 0;


            assertThat(queue).isNotNull();
            assertThat(queue.isEmpty()).isTrue();
            assertThat(queue.getLast()).isEqualTo(first);
            assertThat(queue.getFirst()).isEqualTo(nextFree);
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
            int capacidad = 3;
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(capacidad);
            int first = 0;
            int nextFree = 1;
            int size = 1;

            queue.put(23);

            assertThat(queue).containsExactly(23);
            assertThat(queue.getFirst()).isEqualTo(first);
            assertThat(queue.size()).isEqualTo(size);
            assertThat(queue.getLast()).isEqualTo(nextFree);
        }
        @Test
        @DisplayName("Prueba que se añade un elemento en una posicion intermedia a la cola correctamente")
        public void put_SeAñadeCorrectamenteEnPosicionIntermedia(){
            int capacidad = 3;
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(capacidad);
            int first = 0;
            int nextFree = 2;
            int size = 2;

            queue.put(23);
            queue.put(40);

            assertThat(queue).containsExactly(23,40);

            assertThat(queue.getFirst()).isEqualTo(first);

            assertThat(queue.size()).isEqualTo(size);

            assertThat(queue.getLast()).isEqualTo(nextFree);
        }

        @Test
        @DisplayName("Prueba que se añade un elemento en la ultima posicion de la cola correctamente")
        public void put_SeAñadeCorrectamenteEnLaUltimaPosicion(){
            int capacidad = 3;
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(capacidad);
            int first = 0;
            int nextFree = 0;
            int size = 3;

            queue.put(23);
            queue.put(40);
            queue.put(55);


            assertThat(queue).containsExactly(23,40,55);
            assertThat(queue.getFirst()).isEqualTo(first);
            assertThat(queue.size()).isEqualTo(size);
            assertThat(queue.getLast()).isEqualTo(nextFree);
        }

        @Test
        @DisplayName("Prueba que lanza error cuando se añade un elemento a la cola llena")
        public void put_LaColaEstaLlena_ReturnException(){
            int capacidad = 2;
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(capacidad);

            queue.put(23);
            queue.put(40);

            assertThatThrownBy(() -> queue.put(55))
            .isInstanceOf(FullBoundedQueueException.class)
            .hasMessage("put: full bounded queue");
        }

        @Test
        @DisplayName("Prueba que lanza error cuando se añade un elemento nulo a la cola")
        public void put_ElementoNulo_ReturnException(){
            int capacidad = 2;
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(capacidad);

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
        public void get_SeEliminaCorrectamenteCuandoColaNoLlena(){
            int capacidad = 3;
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(capacidad);
            int first = 1;
            int nextFree = 2;
            int size = 1;

            queue.put(23);
            queue.put(40);

            assertThat(queue.get()).isEqualTo(23);
            assertThat(queue.size()).isEqualTo(size);
            assertThat(queue.getFirst()).isEqualTo(first);
            assertThat(queue.getLast()).isEqualTo(nextFree);
        }

        @Test
        @DisplayName("Prueba que se elimina el ultimo elemento de la cola llena correctamente")
        public void get_SeEliminaCorrectamenteCuandoColaLlena(){
            int capacidad = 2;
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(capacidad);
            int first = 1;
            int nextFree = 0;
            int size = 1;

            queue.put(23);
            queue.put(40);

            assertThat(queue.get()).isEqualTo(23);
            assertThat(queue.size()).isEqualTo(size);
            assertThat(queue.getFirst()).isEqualTo(first);
            assertThat(queue.getLast()).isEqualTo(nextFree);
        }

        @Test
        @DisplayName("Prueba que lanza error cuando se elimina un elemento de la cola vacía")
        public void get_LaColaEstaVacia_ReturnException(){
            int capacidad = 2;
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(capacidad);

            assertThatThrownBy(() -> queue.get())
            .isInstanceOf(EmptyBoundedQueueException.class)
            .hasMessage("get: empty bounded queue");
        }
    }

    @Nested
    @DisplayName("Clase para probar el iterador")
    public class IteratorTest {
        @Test
        @DisplayName("Prueba que se recorre la cola hasta posicion intermedia correctamente")
        public void iterator_RecorreHastaPosicionIntermediaHasNextEsTrue(){
            int capacidad = 3;
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(capacidad);
            queue.put(23);
            queue.put(40);
            queue.put(55);

            Iterator<Integer> it = queue.iterator();
            int aux=it.next();

            assertThat(aux).isEqualTo(23);
            assertThat(it.hasNext()).isTrue();
        }

        @Test
        @DisplayName("Prueba que se recorre la cola hasta la ultima posicion correctamente")
        public void iterator_HasNextEsFalseEnLaUltimaPosicion(){
            int capacidad = 3;
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(capacidad);
            queue.put(23);
            queue.put(40);
            queue.put(55);
            Iterator<Integer> it = queue.iterator();

            it.next();
            it.next();
            int aux=it.next();

            assertThat(aux).isEqualTo(55);
            assertThat(it.hasNext()).isFalse();
        }

        @Test
        @DisplayName("Prueba que hasNext es false con la cola vacia")
        public void iterator_HasNextEsFalseConlaColaVacia(){
            int capacidad = 2;
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(capacidad);

            Iterator<Integer> it = queue.iterator();

            assertThat(it.hasNext()).isFalse();
        }

        @Test
        @DisplayName("Prueba que lanza error cuando se llama a next con la cola vacia")
        public void iterator_LlamaANextConLaColaVacia_ReturnException(){
            int capacidad = 2;
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(capacidad);

            Iterator<Integer> it = queue.iterator();

            assertThatThrownBy(() -> it.next())
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("next: bounded queue iterator exhausted");
        }

        @Test
        @DisplayName("Prueba que lanza error cuando se llama a next en la ultima posicion de la cola")
        public void iterator_LlamaANextEnLaUltimaPosicion_ReturnException(){
            int capacidad = 2;
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(capacidad);
            queue.put(23);
            queue.put(40);

            Iterator<Integer> it = queue.iterator();

            it.next();
            it.next();

            assertThatThrownBy(() -> it.next())
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("next: bounded queue iterator exhausted");
        }
    }

}
