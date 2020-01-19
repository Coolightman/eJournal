package com.coolightman.app.unit.service;

import com.coolightman.app.model.AClass;
import com.coolightman.app.repository.AClassRepository;
import com.coolightman.app.service.PupilService;
import com.coolightman.app.service.impl.AClassServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AClassServiceImplTest {

    @InjectMocks
    private AClassServiceImpl aClassService;

    @Mock
    private AClassRepository aClassRepository;

    @Mock
    private PupilService pupilService;

    @Test
    public void testFindByName(){
        final AClass aClass = new AClass();
        when(aClassRepository.findByNameIgnoreCase(any(String.class))).thenReturn(Optional.of(aClass));
        assertEquals(aClassService.findByName("Vasya"), aClass);
    }

    @Test
    public void testExistByName(){
        final AClass aClass = new AClass();
        final String existName = "Vasya";
        final String notExistName = "Maks";
        aClass.setName(existName);
        when(aClassRepository.existsByNameIgnoreCase(existName)).thenReturn(true);
        when(aClassRepository.existsByNameIgnoreCase(notExistName)).thenReturn(false);

        assertTrue(aClassService.existByName(existName));
        assertFalse(aClassService.existByName(notExistName));
    }

    @Test
    public void testSave(){
        final AClass aClass = new AClass();
        when(aClassRepository.saveAndFlush(aClass)).thenReturn(aClass);

        assertEquals(aClassService.save(aClass), aClass);
    }

    @Test
    public void testUpdate(){
        final AClass aClass = new AClass();
        String name = "Vasya";
        aClass.setId(1L);
        aClass.setName(name);
        when(aClassRepository.saveAndFlush(aClass)).thenReturn(aClass);
        when(aClassRepository.findById(1L)).thenReturn(Optional.of(aClass));

        assertEquals(aClassService.update(aClass), aClass);
    }

    @Test
    public void testFindAll(){
        final List<AClass> aClassList = Collections.singletonList(new AClass());
        when(aClassRepository.findAllByOrderByName()).thenReturn(aClassList);

        assertEquals(aClassService.findAll(), aClassList);
    }

    @Test
    public void testDeleteById(){
        final AClass aClass = new AClass();
        aClass.setId(1L);
        when(aClassRepository.findById(1L)).thenReturn(Optional.of(aClass));
        doNothing().when(aClassRepository).deleteById(any(Long.class));
        assertDoesNotThrow(() -> aClassService.deleteByID(1L));
    }
}
