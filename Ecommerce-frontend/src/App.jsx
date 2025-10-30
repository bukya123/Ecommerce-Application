
import './App.css'
import Products from './Components/Products'
import { BrowserRouter as Router,Routes,Route, BrowserRouter } from 'react-router-dom'
import HomePage from './Pages/HomePage'
import ProductPage from './Pages/ProductsPage'
import Navbar from './Components/Navbar'
import AboutPage from './Pages/AboutPage'
import ContactPage from './Pages/ContactPage'
import { Toaster } from 'react-hot-toast'
import CartPage from './Pages/CartPage'
import LoginPage from './Pages/LoginPage'
import SignUp from './Components/Auth/SIgnUp'
import PrivateRoute from './Components/PrivateRoute'
import Checkout from './Components/Checkout/Checkout'
import PaymentConfirmation from './Components/Checkout/PaymentConfirmation'
function App() {
return (
  <>
  <Router>
    <Navbar/>
    <Routes>
      <Route path='/' element={<HomePage/>}/>
      <Route path='/products' element={<ProductPage/>}/>
      <Route path="/about" element={<AboutPage/>}/>
      <Route path="/contact" element={<ContactPage/>}/>
      <Route path="/cart" element={<CartPage/>}/>
      
      

      <Route path="/" element={<PrivateRoute />}>
      <Route path="/checkout" element={<Checkout/>}/>
      <Route path='/order-confirm' element={ <PaymentConfirmation />}/>
      </Route>

      <Route path="/" element={<PrivateRoute publicPage/>}>
      <Route path="/login" element={<LoginPage/>}/>
      <Route path="/SignUp" element={<SignUp/>}/>
      </Route>

    </Routes>
  </Router>
  <Toaster position='bottom-center'/>
  </>
  
)

}

export default App
