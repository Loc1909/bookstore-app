import { useState } from 'react'
import './App.css'
import LoginPage from './pages/LoginPage'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import AuthLayout from './layouts/AuthLayout'
import DashboardLayout from './layouts/DashboardLayout'
import DashboardPage from './pages/DashboardPage'
import UserTableLayout from './layouts/UserTableLayout'
import UserTablePage from './pages/UserTablePage'
import FormLayout from './layouts/FormLayout'
import FormPage from './pages/FormPage'
import FormProfileLayout from './layouts/FormProfileLayout'
import FormProfilePage from './pages/FormProfilePage'

function App() {
  return (
    <>
      <div className="min-h-dvh">
        <BrowserRouter>
          <Routes>
            <Route element={<AuthLayout />}>
              <Route path="/login" element={<LoginPage />} />
            </Route>
            <Route element={<DashboardLayout />}>
              <Route path="/" element={<DashboardPage />} />
            </Route>
            <Route element={<DashboardLayout />}>
              <Route path="/dashboard" element={<DashboardPage />} />
            </Route>
            <Route element={<UserTableLayout />}>
              <Route path="/users" element={<UserTablePage />} />
            </Route>
            <Route element={<UserTableLayout />}>
              <Route path="/tables" element={<UserTablePage />} />
            </Route>
            <Route element={<FormLayout />}>
              <Route path="/forms/basic" element={<FormPage />} />
            </Route>
            <Route element={<FormProfileLayout />}>
              <Route path="/forms/profile" element={<FormProfilePage />} />
            </Route>
          </Routes>
        </BrowserRouter>
      </div>
    </>
  )
}

export default App;
