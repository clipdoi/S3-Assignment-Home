package com.s3.friendsmanagement.service;

import com.s3.friendsmanagement.exception.DataNotFoundException;
import com.s3.friendsmanagement.exception.InputInvalidException;
import com.s3.friendsmanagement.exception.StatusException;
import com.s3.friendsmanagement.model.User;
import com.s3.friendsmanagement.model.UserRelationship;
import com.s3.friendsmanagement.model.UserRelationshipId;
import com.s3.friendsmanagement.payload.request.CreateFriendConnectionReq;
import com.s3.friendsmanagement.payload.request.EmailRequest;
import com.s3.friendsmanagement.payload.request.RetrieveRequest;
import com.s3.friendsmanagement.payload.request.SubscribeAndBlockRequest;
import com.s3.friendsmanagement.repository.UserRelationshipRepository;
import com.s3.friendsmanagement.repository.UserRepository;
import com.s3.friendsmanagement.utils.EStatus;
import com.s3.friendsmanagement.utils.EmailUtils;
import com.s3.friendsmanagement.utils.ErrorConstraints;
import com.s3.friendsmanagement.utils.RequestValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.zip.DataFormatException;

@Service
public class RelationServiceImp implements RelationService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRelationshipRepository userRelationshipRepository;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addFriend(CreateFriendConnectionReq createFriendConnectionReq) {

        User email = findByEmail(createFriendConnectionReq.getFriends().get(0));
        User friendEmail = findByEmail(createFriendConnectionReq.getFriends().get(1));
        if(email == null && friendEmail == null) {
            throw new DataNotFoundException(ErrorConstraints.EMAIL_NOT_FOUND);
        }

        Optional<UserRelationship> friendRelationship = userRelationshipRepository
                .findByUserRelationship(email.getId(), friendEmail.getId());

        if (friendRelationship.isPresent()) {
            if (friendRelationship.get().getId().getStatus().contains(EStatus.BLOCK.name())) {
                throw new StatusException("This email has been blocked !");
            }
            if (friendRelationship.get().getId().getStatus().contains(EStatus.FRIEND.name())) {
                throw new StatusException("They have already being friend.");
            }
        }

        try {
            UserRelationshipId userRelationshipId = new UserRelationshipId(email.getId(), friendEmail.getId(), EStatus.FRIEND.name());
            UserRelationship relationship = UserRelationship.builder().id(userRelationshipId).build();

            UserRelationshipId userRelationshipIdInverse = new UserRelationshipId(friendEmail.getId(), email.getId(), EStatus.FRIEND.name());
            UserRelationship inverseRelationship = UserRelationship.builder().id(userRelationshipIdInverse).build();

            userRelationshipRepository.save(relationship);
            userRelationshipRepository.save(inverseRelationship);

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public List<String> retrieveFriendsList(EmailRequest emailRequest) {

        User email = findByEmail(emailRequest.getEmail());
        if(email == null){
            throw new DataNotFoundException(ErrorConstraints.EMAIL_NOT_FOUND);
        }
        return userRepository.getListFriendEmails(email.getId());
    }

    @Override
    public List<String> getCommonFriends(CreateFriendConnectionReq friendRequest) {

        User requestEmail = findByEmail(friendRequest.getFriends().get(0));
        User targetEmail = findByEmail(friendRequest.getFriends().get(1));
        if(requestEmail == null && targetEmail == null) {
            throw new DataNotFoundException(ErrorConstraints.EMAIL_NOT_FOUND);
        }

        return userRepository.getCommonFriends(requestEmail.getId(), targetEmail.getId());
    }

    @Override
    public Boolean subscribeTo(SubscribeAndBlockRequest subscribeRequest) {

        User requestEmail = findByEmail(subscribeRequest.getRequester());
        User targetEmail = findByEmail(subscribeRequest.getTarget());
        if(requestEmail == null && targetEmail == null) {
            throw new DataNotFoundException(ErrorConstraints.EMAIL_NOT_FOUND);
        }

        Optional<UserRelationship> friendRelationship = userRelationshipRepository
                .findByUserRelationship(requestEmail.getId(), targetEmail.getId());

        if (friendRelationship.isPresent()) {
            if (friendRelationship.get().getId().getStatus().contains(EStatus.BLOCK.name())) {
                throw new StatusException("Target email has been blocked !");
            }
            if (friendRelationship.get().getId().getStatus().contains(EStatus.SUBSCRIBE.name())) {
                throw new StatusException("Already subscribed to this target email !");
            }
            if (friendRelationship.get().getId().getStatus().contains(EStatus.FRIEND.name())) {
                throw new StatusException("Already being friend of this target, no need to subscribe !");
            }
        }

        UserRelationshipId userRelationshipId = new UserRelationshipId(requestEmail.getId(), targetEmail.getId(), EStatus.FRIEND.name());
        UserRelationship relationship = UserRelationship.builder().id(userRelationshipId).build();

        userRelationshipRepository.save(relationship);
        return true;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean blockEmail(SubscribeAndBlockRequest subscribeRequest) {

        User requestEmail = findByEmail(subscribeRequest.getRequester());
        User targetEmail = findByEmail(subscribeRequest.getTarget());
        if(requestEmail == null && targetEmail == null) {
            throw new DataNotFoundException(ErrorConstraints.EMAIL_NOT_FOUND);
        }

        Optional<UserRelationship> blockedRelation = userRelationshipRepository.findByUserRelationship(requestEmail.getId(), targetEmail.getId());
        if (blockedRelation.isPresent()) {
            if(blockedRelation.get().getId().getStatus().equals("BLOCK")) {
                throw new StatusException("This email has already being blocked !");
            }
        }
        int row = userRelationshipRepository.updateStatusByEmailIdAndFriendId(requestEmail.getId(), targetEmail.getId());
        if(row != 1) {
            throw new StatusException("Error server !");
        }
        return true;
    }

    @Override
    public Set<String> retrieveEmails(RetrieveRequest retrieveRequest) {
        User senderEmail = findByEmail(retrieveRequest.getSender());
        if(senderEmail == null) {
            throw new DataNotFoundException(ErrorConstraints.EMAIL_NOT_FOUND);
        }

        Set<String> emailList = EmailUtils.getEmailsFromText(retrieveRequest.getText());

        emailList = userRepository.getEmailFromSet(emailList);

        List<String> listRetrievableEmail = userRepository.getRetrievableEmail(senderEmail.getId());

        emailList.addAll(listRetrievableEmail);
        return emailList;
    }

}
