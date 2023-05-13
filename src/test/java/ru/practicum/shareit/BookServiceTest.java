package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;

class OrderServiceTest {

    @Test
    void testSaveOrder() {
        OrderService orderService = new OrderService();

        CustomerService customerService = Mockito.mock(CustomerService.class);
        BookService bookService = Mockito.mock(BookService.class);
        OrderDao orderDao = Mockito.mock(OrderDao.class);

        orderService.setCustomerService(customerService);
        orderService.setBookService(bookService);
        orderService.setOrderDao(orderDao);

       // orderService.saveOrder(2,5, 1, );

        Mockito.when(customerService.customerIsBlocked(anyInt())).thenReturn(false);
        Mockito.when(customerService.getCustomerAddress(anyInt())).thenReturn("адрес");

        LocalDate orderDate = LocalDate.of(2022, 4, 12);
        LocalDate deliveryDate = LocalDate.of(2022, 4, 14);
        orderService.saveOrder(2, 5, 1, orderDate);

        Mockito.verify(orderDao, Mockito.times(1))
                .saveOrder(2, "адрес", 5,1, deliveryDate);
        Mockito.verify(customerService, Mockito.times(1))
                .getCustomerAddress(5);
    //    Mockito.verify(bookService,Mockito.times(1))
    //            .decreaseBookAvailableAmount(5,1);
    }

}

class BookService {
    public void decreaseBookAvailableAmount(int bookId, int amount) {
        //уменьшаем доступное количество единиц в БД на 'amount'
    }
}

class CustomerService {

    public boolean customerIsBlocked(int customerId) {
        //проверяем, нет ли у пользователя неоплаченных заказов
        return false;
    }

    public String getCustomerAddress(int customerId) {
        //получаем адрес доставки из базы данных
        return "address";
    }
}

class OrderDao {
    public void saveOrder(int customerId, String deliveryAddress, int bookId, int amount, LocalDate deliveryDate) {
        //сохраняем заказ в базу данных
    }
}

class OrderService {

    CustomerService customerService;
    BookService bookService;
    OrderDao orderDao;

    public void saveOrder(int customerId, int bookId, int amount, LocalDate orderDate) {
        if (customerService.customerIsBlocked(customerId)) {
            throw new CustomerIsBlockedException();
        }

        String customerAddress = customerService.getCustomerAddress(customerId);
        LocalDate deliveryDate = orderDate.plusDays(2);

        orderDao.saveOrder(customerId, customerAddress, bookId, amount, deliveryDate);
        bookService.decreaseBookAvailableAmount(bookId, amount);
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

}

class CustomerIsBlockedException extends RuntimeException {
}