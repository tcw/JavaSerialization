package tcw.serialzation.plain;


import com.google.common.base.Stopwatch;
import org.junit.Test;
import tcw.domain.Employee;

import tcw.serialzation.HelperUtils;
import tcw.serialzation.Populator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static tcw.serialzation.Populator.POPULATION_SIZE;

public class PlainSerializationTest {

    @Test
    public void validation() throws Exception {
        PlainSerialization plainSerialization = new PlainSerialization();
        Employee employee = Populator.employee();
        byte[] serializedEmployee = plainSerialization.serialize(employee);
        Employee deserializedEmployee = plainSerialization.deserialize(serializedEmployee);
        System.out.println(deserializedEmployee);
    }

    @Test
    public void serializationSize() throws Exception {
        PlainSerialization plainSerialization = new PlainSerialization();
        Employee employee = Populator.employee();
        byte[] serializedEmployee = plainSerialization.serialize(employee);
        String hex = HelperUtils.prettyHex16(serializedEmployee);
        System.out.println("SIZE: " + serializedEmployee.length + " bytes");
        System.out.println(hex);
        //60 bytes
    }

    public long benchmark() throws Exception {
        List<Employee> employees = Populator.employees(POPULATION_SIZE);
        Stopwatch stopwatch = Stopwatch.createStarted();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream os = new ObjectOutputStream(bos)) {
            for (Employee employee : employees) {
                os.writeObject(employee);
            }
        }
        byte[] bytes = bos.toByteArray();

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Employee employeeDeserialized = new Employee();
        try (ObjectInputStream objectInputStream = new ObjectInputStream(bis)) {
            for (int i = 0; i < employees.size(); i++) {
                employeeDeserialized = (Employee) objectInputStream.readObject();
            }
        }
        stopwatch.stop();
        return stopwatch.elapsed(TimeUnit.NANOSECONDS);
    }
}
