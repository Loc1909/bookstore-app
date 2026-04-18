# Bookstore App

A native Android application written in Java that serves as a mobile bookstore platform. The app supports features for both regular customers and administrators, and uses a local SQLite database for data persistence. It also includes integration with ZaloPay for checkout processing.

## 🌟 Key Features

### For Users 🛒
- **Authentication**: Register and login to the system.
- **Browse & Search Books**: Explore books by categories or view detailed information, including price, author, description, and ratings.
- **Shopping Cart**: Add books to the cart and manage quantities.
- **Checkout**: Place orders and process payments. Integrated with ZaloPay API for payment execution.
- **Order Tracking**: View order history and order statuses.
- **Reviews**: Add ratings and comment on books.
- **Account Management**: Update user profile information.

### For Administrators 🛡️
- **Admin Dashboard**: Overview of system metrics.
- **Book Management**: Add new books, update existing book data, or delete books. Included stock management.
- **Category Management**: Edit, add, or delete product categories.
- **User Management**: View and supervise user accounts.
- **Order Management**: Monitor and process customer orders. Update order status.
- **Statistics**: View sales and data statistics visualized beautifully using charts.

## 🛠️ Tech Stack

- **Platform**: Android SDK (minSdk 24, targetSdk 36)
- **Language**: Java
- **Database**: SQLite (built-in Android `SQLiteOpenHelper`)
- **Libraries**:
  - **[Glide](https://github.com/bumptech/glide)**: Fast and efficient image loading.
  - **[MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)**: For rendering statistics charts in the admin page.
  - **[Gson](https://github.com/google/gson)**: JSON serialization/deserialization.
  - **[OkHttp3](https://square.github.io/okhttp/)**: Handling HTTP requests (ZaloPay API communication).
  - **UI**: Material Design Components, RecyclerView, CardView.

## 📂 Project Structure

The codebase follows the MVC pattern adjusted for Android apps:
- `activities`: Contains all the Android Activity classes (UI controllers).
- `fragments`: Contains UI Fragments (for Bottom Navigation routing).
- `adapters`: Custom adapters for mapping data arrays to UI components (e.g., RecyclerView).
- `models`: Plain Old Java Objects (POJOs) representing database entities (Book, User, Order, Category, etc.).
- `database`: Contains the `DatabaseHelper` class managing SQLite tables and relationships.
- `dao`: Data Access Objects to interact securely with the SQLite database.
- `api`: Handles server and third-party interactions (like the ZaloPay MakeOrder payload creation).
- `constant`/`helper`/`utils`: Global configurations, shared utility functions, and shared constants.

## 🗄️ Database ERD Summary

The standard local SQLite database (`bookstore.db`) contains the following interconnected tables:
- `users`: User profiles and roles (`admin` or `user`).
- `categories`: Book category names.
- `books`: Stores all book data, referencing the `categories` table.
- `carts` & `cart_items`: Manages the current shopping session for each user.
- `orders` & `order_items`: Tracks processed orders, items inside them, and payment status.
- `reviews`: Cross-references users and books.

## 🚀 Getting Started

### Prerequisites
- Android Studio (Electric Eel or newer recommended)
- JDK 11 or higher
- An Android Device or Emulator running API level 24 or higher.

### Installation
1. Clone the repository or download the source code locally.
2. Open the project folder `bookstore-app` in Android Studio.
3. Allow Gradle to sync and download all dependencies.
4. Run the project onto an emulator or physical device via the play button. 

### Testing Credentials
By default, the application seeds test data.
- **Admin**: `admin@gmail.com` / `123`
- **User**: `user@gmail.com` / `123`

---
*Created as part of the Mobile App Development coursework.*