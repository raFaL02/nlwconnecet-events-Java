package com.hackesociety.events.services;

import com.hackesociety.events.dto.SubscriptionRankingByUser;
import com.hackesociety.events.dto.SubscriptionRankingItem;
import com.hackesociety.events.dto.SubscriptionResponse;
import com.hackesociety.events.exception.EventNotFoundException;
import com.hackesociety.events.exception.SubscriptionConflictExcpetion;
import com.hackesociety.events.exception.UserIndicatorNotFoundException;
import com.hackesociety.events.models.Event;
import com.hackesociety.events.models.Subscription;
import com.hackesociety.events.models.User;
import com.hackesociety.events.repositories.EventRepository;
import com.hackesociety.events.repositories.SubscriptionRepository;
import com.hackesociety.events.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class SubscriptionService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    public SubscriptionResponse createNewSubscription(String eventName, User user, Integer userId) {
        //recuperar evento pelo nome
        Event event = eventRepository.findByPrettyName(eventName);
        if (event == null) {
            throw new EventNotFoundException("Evento " + eventName + " não existe!");
        }

        User userRec = userRepository.findByEmail(user.getEmail());
        if (userRec == null) {
            userRec = userRepository.save(user);
        }

        User indicator = null;
        if (userId != null) {
            indicator = userRepository.findById(userId).orElse(null);
            if (indicator == null) {
                throw new UserIndicatorNotFoundException("Usuário " + userId + " indicador não existe");
            }
        }

        Subscription subscription = new Subscription();
        subscription.setEvent(event);
        subscription.setSubscriber(userRec);
        subscription.setIndication(indicator);

        Subscription tmpSub = subscriptionRepository.findByEventAndSubscriber(event, userRec);
        if (tmpSub != null) {
            throw new SubscriptionConflictExcpetion("Já existe uma inscrição para o usuário " + userRec.getName() + " no evento " + event.getTitle());
        }

        Subscription res = subscriptionRepository.save(subscription);

        return new SubscriptionResponse(res.getSubscriptionNumber(), "http://javasummer.com/subscription" + res.getEvent().getPrettyName() + "/" + res.getSubscriber().getId());
    }

    public List<SubscriptionRankingItem> getCompleteRanking(String prettyName) {
        Event event = eventRepository.findByPrettyName(prettyName);
        if (event == null) {
            throw new EventNotFoundException("Ranking do evento " + prettyName + " não existe!");
        }
        return subscriptionRepository.generateRanking(event.getEventId());
    }

    public SubscriptionRankingByUser getRankingByUser(String prettyName, Integer userId) {
        List<SubscriptionRankingItem> rankingItems = getCompleteRanking(prettyName);

        SubscriptionRankingItem item = rankingItems.stream().filter(i -> i.userId().equals(userId)).findFirst().orElse(null);
        if (item == null) {
            throw new UserIndicatorNotFoundException("Não há inscrições com indicação do usuário " + userId);
        }
        Integer position = IntStream.range(0, rankingItems.size())
                .filter(pos -> rankingItems.get(pos).userId().equals(userId))
                .findFirst().getAsInt();
        return new SubscriptionRankingByUser(item, position + 1);
    }
}
