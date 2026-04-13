
Phone interveiw (45min)

product introduction, self introduction
merge intervals
Onsite (3 rounds)
card game
given a deck of card and several players
for each round, player win if the very first card in hand is larger than others
if tied, compare the fifith card
winner of each round collect cards from rest of participants
write the code to simulate the game
distributed tiny url, discussed about sharding, availability problem
resource acess control
resource represented as tree structure
given two api revoke(resourceId), grant(resourceId)
if the resource got revoked/granted, all its child resources get revoked/granted
implement hasAccess(resourceId)

### Card Game
The Palantir card game interview question is a coding simulation task, often for Forward Deployed Software Engineer roles. It requires simulating a multi-player "War"-style game where players play one card per round from a five-card hand, the highest card wins, and you must track the score or number of wins for each player. 
`
Key Aspects of the Question:
Scenario: Multiple players (often 4), each with a hand of 5 cards.
Gameplay: Players place one card in the middle; the highest card wins that round.
Tie-breaking: Rules may require looking at a specific card in hand (e.g., the fifth card) in case of ties.
Objective: Calculate the total wins for each player or simulate the game to determine the winner.
Implementation: Typically requires modelling players and card comparison logic using data structures like a deck or queue. 

This problem often appears in coding interviews to evaluate programming fundamentals, simulation skills, and the ability to handle logic constraints. 

```python
from collections import defaultdict

value_mapping = {'A': 14, 'K': 13, 'Q': 12, 'J': 11}
suit_mapping = {'S': 4, 'H': 3, 'D': 2, 'C': 1}

# this is sorting in python does not take comparator (cmp_card)
def get_card_val(card):
    return card_digit(card) * 10 + suit_mapping[card[-1]]

def card_digit(card):
    # 修正：处理 '10H' 这种两位数的情况，用切片更稳妥
    rank_str = card[:-1]
    return value_mapping.get(rank_str, int(rank_str))

# compare should return -1/0/1 instead of card (otherwise it's called getMin!!!)
def cmp_card(card1, card2):
    v1, v2 = card_digit(card1), card_digit(card2)
    if v1 != v2:
        return 1 if v1 > v2 else -1
    
    s1, s2 = suit_mapping[card1[-1]], suit_mapping[card2[-1]]
    if s1 != s2:
        return 1 if s1 > s2 else -1
    return 0

def card_game(hands):
    if not hands: return {}
    
    user_points = defaultdict(int)
    users = list(hands.keys())
    
    for user, cards in hands.items():
        cards.sort(key=lambda c: get_card_val(c), reverse = True)
    
    # 假设每人手牌一样多
    rounds = len(hands[users[0]])
    
    for i in range(rounds):
        max_card = None  # 每一轮初始化为空
        winner = None
        
        for user in users:
            # 所有人在这里公平地弹出当前轮次的牌
            card = hands[user].pop(0) 
            
            # 第一次比较，或者当前牌更大时更新
            if max_card is None or cmp_card(card, max_card) > 0:
                max_card = card
                winner = user
        
        user_points[winner] += 1
        
    return dict(user_points)

# 测试
input_hands = {
    'Alice': ['10H', '8S', 'AD'], 
    'Bob': ['JS', '3H', 'AS'], 
    'Carol': ['7C', 'QD', '4D']
}
print(card_game(input_hands))
```