# Spring Boot MongoDB Example

TODO: Add documentation

# REST  Controllers
There are a few different controllers showing various examples of endpoints to use


```
{
  _id: ObjectId(...),
  userId: uniqueUserId,
  date: ISODate(...),
  val: [
    {
      dim: {
        rep: XXX, *,
        dob_yr: 1111, *,
        acct_type: Joint, IRA, Trust, RothIRA, RolloverIRA, *
        state: *
        direction:  in, out, *,
        acct_status: Existing, New, Closures, *
      }, 
      value: 1111.11
    },...
  ]
}
```