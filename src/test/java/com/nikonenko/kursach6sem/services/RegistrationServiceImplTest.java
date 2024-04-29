package com.nikonenko.kursach6sem.services;

import com.nikonenko.kursach6sem.dto.requests.RegisterDto;
import com.nikonenko.kursach6sem.models.Person;
import com.nikonenko.kursach6sem.models.User;
import com.nikonenko.kursach6sem.repositories.PersonRepository;
import com.nikonenko.kursach6sem.repositories.UserRepository;
import com.nikonenko.kursach6sem.services.impl.RegistrationServiceImpl;
import com.nikonenko.kursach6sem.utils.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.naming.Binding;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Test
    public void testRegister() {
        RegisterDto registerDto = new RegisterDto("test", "test", "1234567890", "John");

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);

        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encoded_password");
        when(userRepository.save(any())).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });
        doNothing().when(userValidator).validate(any(), any());
        when(personRepository.save(any())).thenAnswer(invocation -> {
            Person person = invocation.getArgument(0);
            person.setUserId(1L);
            return person;
        });
        doReturn(Person.builder().userId(1L).build())
                .when(modelMapper)
                .map(any(RegisterDto.class), eq(Person.class));

        String redirectUrl = registrationService.register(registerDto, bindingResult);

        assertEquals("redirect:/api/v1/auth/login", redirectUrl);
        verify(userRepository).save(any());
        verify(personRepository).save(any());
    }
}
