package org.excelreader.services;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.StreamSupport;

@Service
public class KthMinFindService {

    private double quickSelect(double[] arr, int k) {
        return quickSelect(arr, 0, arr.length - 1, k-1); //k-1 для того чтобы переданный порядковый номер (начиная с 1)
        //преобразовывался к индексу массива(начиная с 0)
    }

    private double quickSelect(double[] arr, int left, int right, int k) {
        if (left == right) return arr[left];

        int pivotIndex = partition(arr, left, right);

        if (k == pivotIndex)
            return arr[k];
        else if (k < pivotIndex)
            return quickSelect(arr, left, pivotIndex - 1, k);
        else
            return quickSelect(arr, pivotIndex + 1, right, k);
    }

    private int partition(double[] arr, int left, int right) {
        double pivot = arr[right];
        int i = left;
        for (int j = left; j < right; j++) {
            if (arr[j] < pivot) {
                double tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
                i++;
            }
        }
        double tmp = arr[i]; arr[i] = arr[right]; arr[right] = tmp;
        return i;
    }

    public double kthMin(int n, String path) throws IOException {
        //В новых версиях Apache POI оборачивать в try-with-resources не нужно, безопасная работа с ресурсами встроена в библиотеке
        Workbook wb = WorkbookFactory.create(Paths.get(path).toAbsolutePath().normalize().toFile());
        Sheet sheet = wb.getSheetAt(0);

        //Лучший вариант коллекции для многопоточной записи
        Queue<Double> values = new ConcurrentLinkedQueue<>();

        //Параллельное чтение файла полезно для больших файлов
        StreamSupport.stream(sheet.spliterator(), true) // true создает parrallelStream для параллельной обработки
                .forEach(row -> {
                    Cell cell = row.getCell(0);
                    if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                        values.add(cell.getNumericCellValue());
                    }
                });
        double[] arr = values.stream().mapToDouble(Double::doubleValue).toArray();
        return quickSelect(arr, n);
    }
}
