import React, { useState } from 'react';
import { NavLink } from 'react-router-dom';

const Sidebar = ({ sidebarToggle, setSidebarToggle }) => {
    const [selected, setSelected] = useState('Dashboard');
    const [activeMenu, setActiveMenu] = useState('dashboard');
    return (
        <aside
            className={`sidebar fixed left-0 top-0 z-9999 flex h-screen w-72.5 flex-col overflow-y-hidden border-r border-gray-200 bg-white px-5 duration-300 ease-linear dark:border-gray-200 dark:bg-white-400 lg:static lg:translate-x-0 ${sidebarToggle ? 'translate-x-0 lg:w-22.5' : '-translate-x-full'
                }`}
        >
            {/* SIDEBAR HEADER */}
            <div
                className={`sidebar-header flex items-center gap-2 pb-7 pt-8 ${sidebarToggle ? 'justify-center' : 'justify-between'
                    }`}
            >
                <a href="/">
                    <span className={`logo ${sidebarToggle ? 'hidden' : ''}`}>
                        <img className="dark:hidden" src="./src/assets/react.svg" alt="Logo" />
                        <img className="hidden dark:block" src="./src/assets/react.svg" alt="Logo" />
                    </span>
                    <img
                        className={`logo-icon ${sidebarToggle ? 'lg:block' : 'hidden'}`}
                        src="./src/assets/react.svg"
                        alt="Logo"
                    />
                </a>
            </div>

            <div className="no-scrollbar flex flex-col overflow-y-auto duration-300 ease-linear">
                <nav className="mt-5 px-4 lg:mt-9 lg:px-6">
                    <div>
                        <h3 className={`mb-4 text-xs font-semibold uppercase leading-5 text-gray-400 ${sidebarToggle ? 'lg:hidden' : ''}`}>
                            MENU
                        </h3>

                        <ul className="mb-6 flex flex-col gap-2">
                            {/* Menu Item Dashboard */}
                            <li>
                                <NavLink
                                    to="/dashboard"
                                    className={({ isActive }) =>
                                        `group relative flex items-center gap-3 rounded-lg px-4 py-3 font-medium duration-300 ease-in-out hover:bg-gray-200 dark:hover:bg-gray-800 ${isActive
                                            ? 'bg-gray-100 text-black dark:bg-blue-400 dark:text-gray-500'
                                            : 'text-gray-500 dark:text-gray-400'
                                        }`
                                    }
                                >
                                    {({ isActive }) => (
                                        <>
                                            <svg
                                                className={`fill-current duration-300 ease-in-out ${isActive ? 'text-blue-600' : ''}`}
                                                width="22" height="22" viewBox="0 0 22 22" fill="none" xmlns="http://www.w3.org/2000/svg"
                                            >
                                                <path d="M11 1.375C5.6875 1.375 1.375 5.6875 1.375 11C1.375 16.3125 5.6875 20.625 11 20.625C16.3125 20.625 20.625 16.3125 20.625 11C20.625 5.6875 16.3125 1.375 11 1.375ZM11 18.875C6.66875 18.875 3.125 15.3312 3.125 11C3.125 6.66875 6.66875 3.125 11 3.125C15.3312 3.125 18.875 6.66875 18.875 11C18.875 15.3312 15.3312 18.875 11 18.875Z" fill="currentColor" />
                                                <path d="M11 6.1875C10.5188 6.1875 10.125 6.58125 10.125 7.0625V11C10.125 11.4812 10.5188 11.875 11 11.875H13.75C14.2312 11.875 14.625 11.4812 14.625 11C14.625 10.5188 14.2312 10.125 13.75 10.125H11.875V7.0625C11.875 6.58125 11.4812 6.1875 11 6.1875Z" fill="currentColor" />
                                            </svg>
                                            <span className={`${sidebarToggle ? 'lg:hidden' : ''}`}>Dashboard</span>
                                        </>
                                    )}
                                </NavLink>
                            </li>

                            {/* Menu Item User-Table */}
                            <li>
                                <NavLink
                                    to="/users"
                                    className={({ isActive }) =>
                                        `group relative flex items-center gap-3 rounded-lg px-4 py-3 font-medium duration-300 ease-in-out hover:bg-gray-200 dark:hover:bg-gray-800 ${isActive
                                            ? 'bg-gray-100 text-black dark:bg-blue-400 dark:text-gray-500'
                                            : 'text-gray-500 dark:text-gray-400'
                                        }`
                                    }
                                >
                                    {({ isActive }) => (
                                        <>
                                            <svg
                                                className={`fill-current duration-300 ease-in-out ${isActive ? 'text-blue-600' : ''}`}
                                                width="22" height="22" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"
                                            >
                                                <path d="M16 21V19C16 17.9391 15.5786 16.9217 14.8284 16.1716C14.0783 15.4214 13.0609 15 12 15H5C3.93913 15 2.92172 15.4214 2.17157 16.1716C1.42143 16.9217 1 17.9391 1 19V21" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                                                <path d="M8.5 11C10.7091 11 12.5 9.20914 12.5 7C12.5 4.79086 10.7091 3 8.5 3C6.29086 3 4.5 4.79086 4.5 7C4.5 9.20914 6.29086 11 8.5 11Z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                                            </svg>
                                            <span className={`${sidebarToggle ? 'lg:hidden' : ''}`}>User-Table</span>
                                        </>
                                    )}
                                </NavLink>
                            </li>
                            <li>
                                <NavLink
                                    to="/forms/basic"
                                    className={({ isActive }) =>
                                        `group relative flex items-center gap-3 rounded-lg px-4 py-3 font-medium duration-300 ease-in-out hover:bg-gray-200 dark:hover:bg-gray-800 ${isActive
                                            ? 'bg-gray-100 text-black dark:bg-blue-400 dark:text-gray-500'
                                            : 'text-gray-500 dark:text-gray-400'
                                        }`
                                    }
                                >
                                    {({ isActive }) => (
                                        <>
                                            <svg
                                                className={`fill-current duration-300 ease-in-out ${isActive ? 'text-blue-600' : ''}`}
                                                width="22" height="22" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"
                                            ><path
                                                    d="M9 12H15M9 16H15M17 21H7C5.89543 21 5 20.1046 5 19V5C5 3.89543 5.89543 3 7 3H12.5858C12.851 3 13.1054 3.10536 13.2929 3.29289L18.7071 8.70711C18.8946 8.89464 19 9.14903 19 9.41421V19C19 20.1046 18.1046 21 17 21Z"
                                                    stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"
                                                />
                                            </svg>
                                            <span className={`${sidebarToggle ? 'lg:hidden' : ''}`}>Form Basic</span>
                                        </>
                                    )}
                                </NavLink>
                            </li>
                            <li>
                                <NavLink
                                    to="/forms/profile"
                                    className={({ isActive }) =>
                                        `group relative flex items-center gap-3 rounded-lg px-4 py-3 font-medium duration-300 ease-in-out hover:bg-gray-200 dark:hover:bg-gray-800 ${isActive
                                            ? 'bg-gray-100 text-black dark:bg-blue-400 dark:text-gray-500'
                                            : 'text-gray-500 dark:text-gray-400'
                                        }`
                                    }
                                >
                                    {({ isActive }) => (
                                        <>
                                            <svg
                                                className={`fill-current duration-300 ease-in-out ${isActive ? 'text-blue-600' : ''}`}
                                                width="22" height="22" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"
                                            ><path
                                                    d="M9 12H15M9 16H15M17 21H7C5.89543 21 5 20.1046 5 19V5C5 3.89543 5.89543 3 7 3H12.5858C12.851 3 13.1054 3.10536 13.2929 3.29289L18.7071 8.70711C18.8946 8.89464 19 9.14903 19 9.41421V19C19 20.1046 18.1046 21 17 21Z"
                                                    stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"
                                                />
                                            </svg>
                                            <span className={`${sidebarToggle ? 'lg:hidden' : ''}`}>Form Profile</span>
                                        </>
                                    )}
                                </NavLink>
                            </li>
                            {/* Menu Item Login */}
                            <li>
                                <NavLink
                                    to="/login"
                                    className={({ isActive }) =>
                                        `group relative flex items-center gap-3 rounded-lg px-4 py-3 font-medium duration-300 ease-in-out hover:bg-gray-200 dark:hover:bg-gray-800 ${isActive
                                            ? 'bg-gray-100 text-black dark:bg-white/10 dark:text-white'
                                            : 'text-gray-500 dark:text-gray-400'
                                        }`
                                    }
                                >
                                    {({ isActive }) => (
                                        <>
                                            <svg
                                                className={`fill-current ${isActive ? 'text-blue-600' : ''}`}
                                                width="22" height="22" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"
                                            >
                                                <path d="M15 12H3M15 12L12 9M15 12L12 15" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                                                <path d="M9 21H17C18.6569 21 20 19.6569 20 18V6C20 4.34315 18.6569 3 17 3H9" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                                            </svg>
                                            <span className={`${sidebarToggle ? 'lg:hidden' : ''}`}>Login</span>
                                        </>
                                    )}
                                </NavLink>
                            </li>
                        </ul>
                    </div>
                </nav>
            </div>
        </aside>
    );
};

export default Sidebar;