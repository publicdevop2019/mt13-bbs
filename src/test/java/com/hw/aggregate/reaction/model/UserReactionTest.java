package com.hw.aggregate.reaction.model;

import com.hw.aggregate.reaction.ReactionRepository;
import com.hw.aggregate.reaction.exception.FieldValidationException;
import com.hw.aggregate.reaction.exception.ReferenceNotFoundException;
import com.hw.aggregate.reaction.exception.ReferenceServiceNotFoundException;
import com.hw.shared.IdGenerator;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserReactionTest {
    String randomStr() {
        return UUID.randomUUID().toString();
    }

    static ReactionRepository reactionRepository;

    @BeforeClass
    public static void setup() {
        reactionRepository = mock(ReactionRepository.class);
    }

    @Test
    public void create() {
        HashMap mock = mock(HashMap.class);
        Query mock2 = mock(Query.class);
        EntityManager mock3 = mock(EntityManager.class);
        IdGenerator mock4 = mock(IdGenerator.class);

        ReferenceService mock1 = mock(ReferenceService.class);
        when(mock.get(any())).thenReturn(mock1);
        when(mock3.createNativeQuery(anyString())).thenReturn(mock2);
        when(mock2.executeUpdate()).thenReturn(1);
        when(mock2.setParameter(anyInt(), any())).thenReturn(mock2);
        when(mock1.existById(anyString())).thenReturn(Boolean.TRUE);
        when(mock4.getId()).thenReturn(0L);
        doReturn(Optional.empty()).when(reactionRepository).findReaction(anyString(), anyString(), any(ReferenceEnum.class), any(ReactionEnum.class));
        UserReaction.createOrUpdate(randomStr(), ReferenceEnum.COMMENT, ReactionEnum.LIKE, mock, reactionRepository, randomStr(), mock3, mock4);
    }

    @Test(expected = ReferenceNotFoundException.class)
    public void create_ref_not_exist() {
        HashMap mock = mock(HashMap.class);
        ReferenceService mock1 = mock(ReferenceService.class);
        when(mock.get(any())).thenReturn(mock1);
        when(mock1.existById(anyString())).thenReturn(Boolean.FALSE);
        doReturn(Optional.empty()).when(reactionRepository).findReaction(anyString(), anyString(), any(ReferenceEnum.class), any(ReactionEnum.class));
        UserReaction.createOrUpdate(randomStr(), ReferenceEnum.COMMENT, ReactionEnum.LIKE, mock, reactionRepository, randomStr(), null, null);
    }

    @Test(expected = ReferenceServiceNotFoundException.class)
    public void create_ref_svc_not_exist() {
        HashMap mock = mock(HashMap.class);
        when(mock.get(any())).thenReturn(null);
        doReturn(Optional.empty()).when(reactionRepository).findReaction(anyString(), anyString(), any(ReferenceEnum.class), any(ReactionEnum.class));
        UserReaction.createOrUpdate(randomStr(), ReferenceEnum.COMMENT, ReactionEnum.LIKE, mock, reactionRepository, randomStr(), null, null);
    }

    @Test(expected = ReferenceServiceNotFoundException.class)
    public void create_missing_refService() {
        UserReaction.createOrUpdate(randomStr(), ReferenceEnum.COMMENT, ReactionEnum.LIKE, new HashMap<>(), reactionRepository, randomStr(), null, null);
    }

    @Test(expected = FieldValidationException.class)
    public void create_null_fields_0() {
        ReactionRepository instance1 = mock(ReactionRepository.class);
        UserReaction.createOrUpdate(null, ReferenceEnum.COMMENT, ReactionEnum.LIKE, new HashMap<>(), instance1, randomStr(), null, null);
    }

    @Test(expected = FieldValidationException.class)
    public void create_null_fields_1() {
        ReactionRepository instance1 = mock(ReactionRepository.class);
        UserReaction.createOrUpdate(randomStr(), null, ReactionEnum.LIKE, new HashMap<>(), instance1, randomStr(), null, null);
    }

    @Test(expected = FieldValidationException.class)
    public void create_null_fields_2() {
        ReactionRepository instance1 = mock(ReactionRepository.class);
        UserReaction.createOrUpdate(randomStr(), ReferenceEnum.COMMENT, null, new HashMap<>(), instance1, randomStr(), null, null);
    }

    @Test(expected = FieldValidationException.class)
    public void create_null_fields_3() {
        ReactionRepository instance1 = mock(ReactionRepository.class);
        UserReaction.createOrUpdate(randomStr(), ReferenceEnum.COMMENT, ReactionEnum.LIKE, null, instance1, randomStr(), null, null);
    }

    @Test(expected = FieldValidationException.class)
    public void create_null_fields_4() {
        UserReaction.createOrUpdate(randomStr(), ReferenceEnum.COMMENT, ReactionEnum.LIKE, new HashMap<>(), null, randomStr(), null, null);
    }

    @Test(expected = FieldValidationException.class)
    public void create_null_fields_5() {
        UserReaction.createOrUpdate(randomStr(), ReferenceEnum.COMMENT, ReactionEnum.LIKE, new HashMap<>(), reactionRepository, null, null, null);
    }
}