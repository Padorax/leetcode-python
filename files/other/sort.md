+ Quick sort
```python
# in-place
def fast_sort(A, l , r):
    if l >= r: return
    i, j = l, r
    # partition the leftmost element A[l] - pivot
    # pointer i prefers num <= A[l]
    # pointer j prefers num >= A[l]
    while i < j:
        while i < j and A[j] >= A[l]: j -= 1
        while i < j and A[i] <= A[l]: i += 1
        A[i], A[j] = A[j], A[i]
    A[i], A[l] = A[l], A[i]
    fast_sort(A, l, i - 1)
    fast_sort(A, i + 1, r)

arr = [3, 1, 2, 6, 5]
fast_sort(arr, 0, len(arr)-1)
```

+ Merge sort
```python
# in-place
def mergeSort(A, tmp, l, r):
    if l >= r: return
    mid = l + (r-l) // 2
    # sort left
    mergeSort(A, tmp, l, mid)
    # sort right
    mergeSort(A, tmp, mid+1, r)
    # merge
    i, j, pos = l, mid+1, l
    while i <= mid and j <= r:
        if A[i] <= A[j]:
            tmp[pos] = A[i]
            i += 1
        else:
            tmp[pos] = A[j]
            j += 1
        pos += 1

    # *1
    while i <= mid:
        tmp[pos] = A[i]
        pos += 1
        i += 1
    while j <= r:
        tmp[pos] = A[j]
        pos += 1
        j += 1

    # *2
    # for k in range(i, mid+1):
    #     tmp[pos] = A[k]
    #     pos += 1
    # for k in range(j, r+1):
    #     tmp[pos] = A[k]
    #     pos += 1

    # *3
    # if i <= mid:
    #     tmp[pos:r+1] = A[i:mid+1]
    # else:
    #     tmp[pos:r+1] = A[j:r+1]

    A[l:r+1] = tmp[l:r+1]

lst = [3,1,9,2,7,10]
copy = [0] * len(lst)
mergeSort(lst, copy, 0, len(lst)-1)
```