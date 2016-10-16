# Code Template Repository Client

## Config

### Version
Check the version of client which is instelled.

```bash
repo -v
```

### Default Values
Manage default values to the settings file. Those values will be used in the next template installations.

#### Adding default values

```bash
repo config default group com.example
repo config default artifact example
repo config default version 1.0.0
repo config default packaging jar
```

#### Removing default values

```bash
repo config remove-default group
repo config remove-default version
```

#### Displaying default values

```bash
repo config default

## default
	group = com.example
	artifact = example
	version = 1.0.0
	packaging = jar
```

### Dependency Aliases
Manage aliases for the dependencies you use frequently.

```bash
repo alias 
repo config default artifact example
repo config default version 1.0.0
repo config default packaging jar
``` 

