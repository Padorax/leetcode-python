Check if is/contains permutation/anagram:
1. Build count map, check for each key, count match/exceeds. 
window match is how many unique charaters have expected count, needed is distinct number of characters in target
How to update count as window slides? See 76 (just needs to contains permutation)
2. Use 26 letter array
For each letter from a-z, record 1 if count matches otherwise 0, needed is 26.
How to update? See 567 (need to be exact permutation)


Running length
1. keep increasing right pointer till letter changed
2. ```python
for i in range(n):
    if i > 0 and s[i] != s[i-1]:
        res.append(s[start:i])
        start = i
# need to handle tail
res.append(s[start:])
palindrom check
```python
# stop at first unmatch, advantage is even if starting i, j does not satisfy, can handle
# resulting length is (j-i-1)
while i >= 0 and j < n and word[i] == word[j]:
    i += 1
    j -= 1
```
compared to 
```python
# stop at last match, need to additionally check starting i, j
while i-1 >= 0 and j+1 <n and word[i-1] == word[j+1]:
    i -= 1
    j += 1
```
similarly for contain string
```python
while match == target:
    minLen = min(minLen, i-j+1)
    i += 1
    # update match
# stop at first unmatch
```