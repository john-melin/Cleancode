

Issues
- redundant comments

// get the country code
string countryCode = getCountryCode('REMOTE_ADDR');
 
// if country code is US
if (countryCode == 'US') {
    // display the form input for state
    println(formInputState());
}
---------------------------

function canBuyBeer(age, money) {
  if(age >= 21 && money >= 20) {
    return true
  }
  return false
}

Issues
- magical numbers
- complex 
- unnescessary boolean values.

Her er koden kanskje ikke nødvendigvis så vanskelig å lese. Men hva betyr egentlig 21 og 20? Her må man analysere
konteksten av hvilken variabel som brukes mot hvilet tall for å skjønne hva tallet betyr.
Det samme hvis man senere ønsker å endre verdiene. Dette kan gi økt risiko for bugs.

function canBuyBeer(age, money) {
  const legalDrinkingAge = 21
  const beerPrice = 20

  const isOverLegalDrinkingAge = age >= legalDrinkingAge;
  const hasMoneyForBeer = money >= beerPrice;

  return isOverLegalDrinkingAge && hasMoneyForBeer;
}
--------------------------

function shouldShowImage(itemIndex, article, showAllImages) {
  return [0, 1, 2].includes(itemIndex)
    ? !!article.imageUrl
    : showAllImages
      ? !!article.imageUrl
      : false
}

Issues

- Too clever
- magical values
- Do we really need the showAllImages flag?

function shouldShowImage(itemIndex, article, showAllImages) {
  const hasImageUrl = article.imageUrl;
  const imageIndexToShow = [0,1,2];

  if (!hasImageUrl) {
    return false
  }
  if (showAllImages) {
    return true
  }

  return imageIndexToShow.includes(itemIndex)
}

--------------------------

private static List readLines(String fileName) {
  String line;
  List file = new List();

  try {    
    BufferedReader in = new BufferedReader(new FileReader(fileName));
    while ((line = in.readLine()) != null)
      file.add(line);
    in.close();
  } catch (Exception e){
    System.out.println(e);
    return null;
  }
  return file;
}

Issues 
- unnescessary code (also hides potential bugs)
- bad naming (yes you read a file, but what does it contain?. The function says readLines. So should it not rather be lines that you return?)

private static List readLines(String fileName) throws IOException {
  String line;
  List lines = new List();
  BufferedReader reader = new BufferedReader(new FileReader(fileName));

  while ((line = reader.readLine()) != null) {
    lines.add(line);
  }
  reader.close();

  return lines;
}

---------------------------

boolean userEmail = user.email.contains('computas');
if(userEmail) {
    addUser(user);
    alertUser();     
} else {
    alertUser();
}

Issues
- duplicated code.
- bad naming (a boolean looks like it contains a string)
- magical number

The alertUser() function is getting called in both conditional branches so it can be moved out. Less code, simpler code, easier to read

String computasDomain = 'computas';
boolean isEmailInDomain = user.email.contains(computasDomain);
if(isEmailInDomain) {
    addUser(user);
}
alertUser();

---------------------------
String name = "Ryan McDermott";

function splitIntoFirstAndLastName() {
  name = name.split(" ");
}
splitIntoFirstAndLastName();

Issues
- avoid side effects 

function splitIntoFirstAndLastName(name) {
  return name.split(" ");
}

String name = "Ryan McDermott";
String splitName = splitIntoFirstAndLastName(name);

---------------------------

function getProduct(htmlResponse) {
   String title = htmlResponse.css('.title').html();
   String priceHTML = htmlResponse.css('.price').html();
   int price;

   try {
      price = int(priceHTML);
   } except ValueError {
      price = null;
   }

   String sizesHTML = list(htmlResponse.css('.product .size').html());
   String[] sizes = [parseSize(htmlSize) for htmlSize in sizesHTML];
   return sizes;
}

Issues
- too much responsibility
- inconsistent naming (title / priceHTML)

function getProduct(htmlResponse) {
  String title = getTitle(htmlResponse);
  String price = getPrice(htmlResponse);
  String[] sizes = getProductSizes(htmlResponse);
  return sizes;
}

function getTitle(html_response) {
   return htmlResponse.css('.title').html()
}

function getPrice(htmlResponse) {
   String price = htmlResponse.css('.price').html()
   try {
      return int(price)
   } except ValueError {
      return null; 
   }
}

function getProductSizes(htmlResponse) {
   String sizesHTML = list(htmlResponse.css('.product .size').html());
   String[] sizes = [parseSize(htmlSize) for htmlSize in sizesHTML];
   return sizes
}


---------------------------

Her har vi en enkel og noe uferdig versjon av MineSweeper. 

Her kan man anta at myTruth og myShow allerede er initialisert med verdier.
- MyTruth vil inneholde 0 i de tilfellene det ikke er noen mine på gitt index. 
- MyShow vil si noe om cellen på gitt index er skjult eller ikke.

public class MineSweeper {
  private int[][] myTruth;
	private boolean[][] myShow;
	
	public void cellPicked(int row, int col) {
    if (inBounds(row, col) && !myShow[row][col]) {
      myShow[row][col] = true;
		
			if (myTruth[row][col] == 0) {
        for (int r = -1; r <= 1; r++) {
					for (int c = -1; c <= 1; c++) {
						cellPicked(row + r, col + c);
          }
        }
			}	
		}
	}
	
	public boolean inBounds(int row, int col) {
    return 0 <= row && row < myTruth.length && 0 <= col && col < myTruth[0].length;
	}
}

Issues
- bad naming (myShow and myTruth doesnt say much about what they contain. Also cellPicked sounds like a boolean)
- complex conditional
- function doing too much

public class MineSweeper {
  private int[][] board;
	private boolean[][] cellCheckedState;
	
	public void checkCell(int row, int col) {
    boolean isCellChecked = cellCheckedState[row][col];
    if (inBounds(row, col) && !isCellChecked) {
      cellCheckedState[row][col] = true;
      boolean isSafeCell = myTruth[row][col] == 0;

			if (isSafeCell) {
        this.checkAdjecentCells(row, col);
			}	
		}
	}

  public checkAdjecentCells(int row, int col) {
      for (int r = -1; r <= 1; r++) {
        for (int c = -1; c <= 1; c++) {
          pickCell(row + r, col + c);
        }
      }
  }

	public boolean inBounds(int row, int col) {
    boolean isRowWithinBounds = 0 <= row && row < myTruth.length;
    boolean isColumnWithinBounds =  0 <= col && col < myTruth[0].length;
    return isRowWithinBounds && isColumnWithinBounds;
	}
}


---------------------------

Her har vi en klasse som styrer hvilken mengde discount man kan få basert på gitte typer.
Vi har her veldig lite informasjon, så prøv så godt som du kan.

- calculate: Skal regne ut pricen for en gitt kunde.
- type: Skal her stå for en status om kunden som kan si hvor verdifull kunden er. Vi vet veldig lite her, så vi kan bare anta 4 forskjellige nivåer
- years: Hvor mange år personen har vært kunde. Kunden får 1% men maksimalt 5 procent i loyalty discount.
- amount: Den opprinnelige prisen

public class Class1 {
  public decimal calculate(decimal amount, int type, int years) {
    decimal result = 0;
    decimal disc = (years > 5) ? (decimal) 5 / 100 : (decimal) years / 100; 

    if (type == 1) {
      result = amount;
    } else if (type == 2) {
      result = (amount - (0.1m * amount)) - disc;
    } else if (type == 3) {
      result = (amount - (0.5m * amount)) - disc;
    } else if (type == 4) {
      result = (amount - (0.7m * amount) - disc;
    }
    return result;
  }
}

Issues
- magic numbers (make enums and constants)
- bad naming (maybe class1 should be somehting like discount manager, CustomerPriceCalculator)
- functions doing too much
- duplicated code

enum CustomerStatus {
  Basic = 1,
  Loyal = 2,
  Executive = 3,
  Uber = 4
}

const int MAXIMUM_DISCOUNT_FOR_LOYALTY = 5;
const decimal DISCOUNT_FOR_LOYAL_CUSTOMERS = 0.1;
const decimal DISCOUNT_FOR_EXECUTIVE_CUSTOMERS = 0.5;
const decimal DISCOUNT_FOR_UBER_CUSTOMERS = 0.7;

public class DiscountManager {
  public decimal getPriceWithDiscount(decimal price, CustomerStatus customerStatus, int yearsBeingACustomer) {
    decimal discountForLoyalty = getDiscountForLoyalty(yearsBeingACustomer);
    decimal discountForCustomerStatus = getDiscountForCustomerStatus(price, customerStatus);
    decimal discountedPrice =  price - discountForCustomerStatus - discountForLoyalty;
    return discountedPrice;
  }

  function decimal getDiscountForLoyalty(int yearsBeingACustomer) {
    decimal maxDiscountForLoyalty = MAXIMUM_DISCOUNT_FOR_LOYALTY / 100;

    if (yearsBeingACustom > MAXIMUM_DISCOUNT_FOR_LOYALTY) {
      return maxDiscountForLoyalty;
    }
    return (decimal) yearsBeingACustomer / 100;
  }

  function decimal getDiscountForCustomerStatus(decimal price, int customerStatus) {
    switch (customerStatus) {
      case CustomerStatus.Basic:
        return price;
      case CustomerStatus.Loyal:
        return getPriceDiscount(DISCOUNT_FOR_LOYAL_CUSTOMERS, price);
      case CustomerStatus.Executive:
        return getPriceDiscount(DISCOUNT_FOR_EXECUTIVE_CUSTOMERS, price);
      case CustomerStatus.Uber:
        return getPriceDiscount(DISCOUNT_FOR_UBER_CUSTOMERS, price);
    }
  }

  function decimal getPriceDiscount(decimal discount, int price) {
    return discount * price;
  }
}