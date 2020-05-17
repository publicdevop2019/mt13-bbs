package com.hw.aggregate.reaction.model;

import com.hw.aggregate.reaction.ReactionRepository;
import com.hw.aggregate.reaction.exception.FieldValidationException;
import com.hw.aggregate.reaction.exception.ReferenceNotFoundException;
import com.hw.aggregate.reaction.exception.ReferenceServiceNotFoundException;
import org.junit.BeforeClass;
import org.junit.Test;

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
        ReferenceService mock1 = mock(ReferenceService.class);
        when(mock.get(any())).thenReturn(mock1);
        when(mock1.existById(anyString())).thenReturn(Boolean.TRUE);
        doReturn(Optional.empty()).when(reactionRepository).findReaction(anyString(), anyString(), any(ReferenceEnum.class), any(ReactionEnum.class));
        UserReaction.create(randomStr(), ReferenceEnum.COMMENT, ReactionEnum.LIKE, mock, reactionRepository, randomStr());
    }

    @Test(expected = ReferenceNotFoundException.class)
    public void create_ref_not_exist() {
        HashMap mock = mock(HashMap.class);
        ReferenceService mock1 = mock(ReferenceService.class);
        when(mock.get(any())).thenReturn(mock1);
        when(mock1.existById(anyString())).thenReturn(Boolean.FALSE);
        doReturn(Optional.empty()).when(reactionRepository).findReaction(anyString(), anyString(), any(ReferenceEnum.class), any(ReactionEnum.class));
        UserReaction.create(randomStr(), ReferenceEnum.COMMENT, ReactionEnum.LIKE, mock, reactionRepository, randomStr());
    }

    @Test(expected = ReferenceServiceNotFoundException.class)
    public void create_ref_svc_not_exist() {
        HashMap mock = mock(HashMap.class);
        when(mock.get(any())).thenReturn(null);
        doReturn(Optional.empty()).when(reactionRepository).findReaction(anyString(), anyString(), any(ReferenceEnum.class), any(ReactionEnum.class));
        UserReaction.create(randomStr(), ReferenceEnum.COMMENT, ReactionEnum.LIKE, mock, reactionRepository, randomStr());
    }

    @Test(expected = ReferenceServiceNotFoundException.class)
    public void create_missing_refService() {
        UserReaction.create(randomStr(), ReferenceEnum.COMMENT, ReactionEnum.LIKE, new HashMap<>(), reactionRepository, randomStr());
    }

    @Test(expected = FieldValidationException.class)
    public void create_null_fields_0() {
        ReactionRepository instance1 = mock(ReactionRepository.class);
        UserReaction.create(null, ReferenceEnum.COMMENT, ReactionEnum.LIKE, new HashMap<>(), instance1, randomStr());
    }

    @Test(expected = FieldValidationException.class)
    public void create_null_fields_1() {
        ReactionRepository instance1 = mock(ReactionRepository.class);
        UserReaction.create(randomStr(), null, ReactionEnum.LIKE, new HashMap<>(), instance1, randomStr());
    }

    @Test(expected = FieldValidationException.class)
    public void create_null_fields_2() {
        ReactionRepository instance1 = mock(ReactionRepository.class);
        UserReaction.create(randomStr(), ReferenceEnum.COMMENT, null, new HashMap<>(), instance1, randomStr());
    }

    @Test(expected = FieldValidationException.class)
    public void create_null_fields_3() {
        ReactionRepository instance1 = mock(ReactionRepository.class);
        UserReaction.create(randomStr(), ReferenceEnum.COMMENT, ReactionEnum.LIKE, null, instance1, randomStr());
    }

    @Test(expected = FieldValidationException.class)
    public void create_null_fields_4() {
        UserReaction.create(randomStr(), ReferenceEnum.COMMENT, ReactionEnum.LIKE, new HashMap<>(), null, randomStr());
    }

    @Test(expected = FieldValidationException.class)
    public void create_null_fields_5() {
        UserReaction.create(randomStr(), ReferenceEnum.COMMENT, ReactionEnum.LIKE, new HashMap<>(), reactionRepository, null);
    }
}