/*
 * AUTORES:
 * María Victoria Huesca Peláez
 * Eduardo García Rivas
 */

package org.boundedqueue;

import static org.assertj.core.api.Assertions.*;

import java.util.Iterator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mps.boundedqueue.ArrayBoundedQueue;
import org.mps.boundedqueue.EmptyBoundedQueueException;
//import org.springframework.test.util.ReflectionTestUtils;
import org.mps.boundedqueue.FullBoundedQueueException;

public class ArrayBoundedQueueTest {
    @Nested
    @DisplayName("Tests para el constructor ArrayBoundedQueue")
    class TestArrayBoundedQueueConstructor {
        @Test
        @DisplayName("Crear un ArrayBoundedQueue con capacidad menor o igual que cero lanza una excepción")
        public void arrayBoundedQueue_CapacidadMenorQueCero_LanzaExcepcion() throws IllegalArgumentException {
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
                new ArrayBoundedQueue<Integer>(-1);
            }).withMessage("ArrayBoundedException: capacity must be positive");

            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
                new ArrayBoundedQueue<Integer>(0);
            }).withMessage("ArrayBoundedException: capacity must be positive");
        }

        @Test
        @DisplayName("Crear un ArrayBoundedQueue con capacidad mayor que cero crea correctamente la cola")
        public void arrayBoundedQueue_CapacidadMayorQueCero_CreaCola() {
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<Integer>(1);
            int first = 0;
            int last = 0;
            int size = 0;

            assertThat(queue).isNotNull();
            assertThat(queue.getFirst()).isEqualTo(first);
            assertThat(queue.getLast()).isEqualTo(last);
            assertThat(queue.size()).isEqualTo(size);
        }
    }

    @Nested
    @DisplayName("Tests para el método put")
    class TestPut {
        @Test
        @DisplayName("Insertar un elemento en una cola llena lanza excepción")
        public void put_ColaLlena_LanzaExcepcion() throws FullBoundedQueueException {
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<Integer>(4);

            queue.put(1);
            queue.put(2);
            queue.put(3);
            queue.put(4);

            assertThatExceptionOfType(FullBoundedQueueException.class).isThrownBy(() -> {
                queue.put(5);
            }).withMessage("put: full bounded queue");
        }

        @Test
        @DisplayName("Insertar un elemento nulo lanza excepción")
        public void put_ValorNulo_LanzaExcepcion() {
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<Integer>(4);

            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
                queue.put(null);
            }).withMessage("put: element cannot be null");
        }

        @Test
        @DisplayName("Insertar elemento en cola vacía inserta correctamente en primera posición")
        public void put_colaVacia_InsertaElementoCorrectamentePrimeraPosicion() {
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<Integer>(3);
            int first = 0;
            int nextFree = 1;
            int size = 1;

            queue.put(1);

            assertThat(queue.getFirst()).isEqualTo(first);
            assertThat(queue.getLast()).isEqualTo(nextFree);
            assertThat(queue.size()).isEqualTo(size);
        }

        @Test
        @DisplayName("Insertar elemento en cola no vacía inserta correctamente en posición intermedia")
        public void put_ColaNoVacia_InsertaCorrectamentePosicionIntermedia() {
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(4);
            int first = 0;
            int nextFree = 3;
            int size = 3;

            queue.put(1);
            queue.put(2);
            queue.put(3);

            assertThat(queue.getFirst()).isEqualTo(first);
            assertThat(queue.getLast()).isEqualTo(nextFree);
            assertThat(queue.size()).isEqualTo(size);
        }

        @Test
        @DisplayName("Insertar elemento en cola no vacía y última posición inserta correctamente")
        public void put_ColaNoVacia_InsertaCorrectamenteUltimaPosicion() {
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(3);
            int first = 0;
            int nextFree = 0;
            int size = 3;

            queue.put(1);
            queue.put(2);
            queue.put(3);

            assertThat(queue.getFirst()).isEqualTo(first);
            assertThat(queue.getLast()).isEqualTo(nextFree);
            assertThat(queue.size()).isEqualTo(size);
        }

    }

    @Nested
    @DisplayName("Tests para el método get")
    class TestGet {
        @Test
        @DisplayName("Extraer último elemento de una cola vacía lanza excepción")
        public void get_ColaVacia_LanzaExcepcion() {
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<Integer>(3);

            assertThatExceptionOfType(EmptyBoundedQueueException.class).isThrownBy(() -> {
                queue.get();
            }).withMessage("get: empty bounded queue");
        }

        @Test
        @DisplayName("Extraer último elemento de una cola llena extrae correctamente el primer elemento")
        public void get_ColaNoVacia_ExtraeCorrectamentePrimerElemento() {
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<Integer>(3);
            int first = 1;
            int nextFree = 0;
            int size = 2;

            queue.put(1);
            queue.put(2);
            queue.put(3);

            int item = queue.get();

            assertThat(item).isEqualTo(1);
            assertThat(queue.getFirst()).isEqualTo(first);
            assertThat(queue.getLast()).isEqualTo(nextFree);
            assertThat(queue.size()).isEqualTo(size);
        }

        @Test
        @DisplayName("Extraer último elemento de una cola no llena extrae correctamente el último elemento")
        public void get_ColaNoLlena_ExtraeCorrectamenteUltimoElemento() {
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<Integer>(3);
            int first = 1;
            int nextFree = 2;
            int size = 1;

            queue.put(1);
            queue.put(2);

            int item = queue.get();

            assertThat(item).isEqualTo(1);
            assertThat(queue.getFirst()).isEqualTo(first);
            assertThat(queue.getLast()).isEqualTo(nextFree);
            assertThat(queue.size()).isEqualTo(size);
        }
    }

    @Nested
@DisplayName("Tests for the iterator method")
class TestIterator {
    @Test
    @DisplayName("Calling iterator on an empty queue returns an empty iterator")
    public void iterator_EmptyQueue_ReturnsEmptyIterator() {
        ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(3);

        Iterator<Integer> iterator = queue.iterator();

        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    @DisplayName("LLamar al iterador en una cola llena recorre correctamente los elementos")
    public void iterator_ColaLLena_RecorreCorrectamente() {
        ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(3);
        queue.put(1);
        queue.put(2);
        queue.put(3);

        Iterator<Integer> iterator = queue.iterator();

        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next()).isEqualTo(1);
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next()).isEqualTo(2);
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next()).isEqualTo(3);
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    @DisplayName("Llamar al iterador con cola parcialmente llena recorre los elementos correctamente")
    public void iterator_ColaParcialmenteLlena_RecorreCorrectamente() {
        ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(3);
        queue.put(1);
        queue.put(2);

        Iterator<Integer> iterator = queue.iterator();

        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next()).isEqualTo(1);
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next()).isEqualTo(2);
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    @DisplayName("Llamar al iterador en una cola vacía hasNext() devuelve false")
    public void iterator_ColaVacia_HasNextDevuelveFalse() {
        ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(3);

        Iterator<Integer> iterator = queue.iterator();

        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    @DisplayName("Llamar al iterador en una cola vacía next lanza excepción")
    public void iterator_ColaVacia_NextLanzaExcepcion() {
        ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(3);

        Iterator<Integer> iterator = queue.iterator();

        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            iterator.next();
        }).withMessage("next: bounded queue iterator exhausted");
    }
}


}
