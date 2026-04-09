import React, { useState } from "react";


export default function UserTable() {
    const [searchTerm, setSearchTerm] = useState("");
    return (
        <><div className="flex flex-col gap-3 p-5 sm:flex-row sm:items-center sm:justify-between">
            <div>
                <h3 className="text-lg font-semibold text-gray-800 dark:text-gray-500">
                    All Users
                </h3>
            </div>

            {/* Search Input */}
            <div className="relative">
                <span className="absolute left-3.5 top-1/2 -translate-y-1/2 text-gray-500 dark:text-gray-400">
                    <svg className="fill-current" width="18" height="18" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path fillRule="evenodd" clipRule="evenodd" d="M9.16667 3.33333C5.94501 3.33333 3.33333 5.94501 3.33333 9.16667C3.33333 12.3883 5.94501 15 9.16667 15C10.7479 15 12.1856 14.3729 13.2395 13.3514L15.6943 15.8062C16.0197 16.1317 16.5473 16.1317 16.8728 15.8062C17.1982 15.4808 17.1982 14.9531 16.8728 14.6277L14.418 12.1729C15.4395 11.1189 16.0667 9.68119 16.0667 9.16667C16.0667 5.94501 13.455 3.33333 9.16667 3.33333ZM5 9.16667C5 6.86548 6.86548 5 9.16667 5C11.4679 5 13.3333 6.86548 13.3333 9.16667C13.3333 11.4679 11.4679 13.3333 9.16667 13.3333C6.86548 13.3333 5 11.4679 5 9.16667Z" />
                    </svg>
                </span>
                <input
                    type="text"
                    placeholder="Search users..."
                    className="w-full rounded-lg border border-gray-200 bg-transparent py-2.5 pl-10 pr-4 text-sm outline-none focus:border-blue-500 dark:border-gray-700 dark:bg-dark-2 dark:text-gray dark:focus:border-blue-500 sm:w-72"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
            </div>
        </div>
            <div className="overflow-hidden rounded-xl border border-gray-200 bg-white dark:border-gray-300 dark:bg-white/3">
                <div className="max-w-full overflow-x-auto">
                    <table className="min-w-full">
                        {/* table header start */}
                        <thead>
                            <tr className="border-b border-gray-100 dark:border-gray-400">
                                <th className="px-5 py-3 sm:px-6">
                                    <div className="flex items-center">
                                        <p className="font-medium text-gray-500 text-theme-xs dark:text-gray-400">
                                            User
                                        </p>
                                    </div>
                                </th>
                                <th className="px-5 py-3 sm:px-6">
                                    <div className="flex items-center">
                                        <p className="font-medium text-gray-500 text-theme-xs dark:text-gray-400">
                                            Project Name
                                        </p>
                                    </div>
                                </th>
                                <th className="px-5 py-3 sm:px-6">
                                    <div className="flex items-center">
                                        <p className="font-medium text-gray-500 text-theme-xs dark:text-gray-400">
                                            Team
                                        </p>
                                    </div>
                                </th>
                                <th className="px-5 py-3 sm:px-6">
                                    <div className="flex items-center">
                                        <p className="font-medium text-gray-500 text-theme-xs dark:text-gray-400">
                                            Status
                                        </p>
                                    </div>
                                </th>
                                <th className="px-5 py-3 sm:px-6">
                                    <div className="flex items-center">
                                        <p className="font-medium text-gray-500 text-theme-xs dark:text-gray-400">
                                            Budget
                                        </p>
                                    </div>
                                </th>
                            </tr>
                        </thead>
                        {/* table header end */}
                        {/* table body start */}
                        <tbody className="divide-y divide-gray-100 dark:divide-gray-800">

                            <tr>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <div className="flex items-center gap-3">
                                            <div className="w-10 h-10 overflow-hidden rounded-full">
                                                <img src="./src/assets/country-03.svg" alt="brand" />
                                            </div>
                                            <div>
                                                <span className="block font-medium text-gray-800 text-theme-sm dark:text-red-500">
                                                    Dung Truong
                                                </span>
                                                <span className="block text-gray-500 text-theme-xs dark:text-gray-400">
                                                    Bug Maker
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <p className="text-gray-500 text-theme-sm dark:text-gray-400">
                                            Open U
                                        </p>
                                    </div>
                                </td>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <div className="flex -space-x-2">
                                            <div className="w-6 h-6 overflow-hidden border-2 border-white rounded-full dark:border-gray-900">
                                                <img src="./src/assets/country-03.svg" alt="user" />
                                            </div>
                                        </div>
                                    </div>
                                </td>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <p className="rounded-full bg-success-50 px-2 py-0.5 text-theme-xs font-medium text-success-700 dark:bg-success-500/15 dark:text-success-500">
                                            Active
                                        </p>
                                    </div>
                                </td>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <p className="text-gray-500 text-theme-sm dark:text-gray-400">
                                            365.67K
                                        </p>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <div className="flex items-center gap-3">
                                            <div className="w-10 h-10 overflow-hidden rounded-full">
                                                <img src="./src/assets/country-01.svg" alt="brand" />
                                            </div>
                                            <div>
                                                <span className="block font-medium text-gray-800 text-theme-sm dark:text-red-500">
                                                    Lindsey Curtis
                                                </span>
                                                <span className="block text-gray-500 text-theme-xs dark:text-gray-400">
                                                    Web Designer
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <p className="text-gray-500 text-theme-sm dark:text-gray-400">
                                            Agency Website
                                        </p>
                                    </div>
                                </td>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <div className="flex -space-x-2">
                                            <div className="w-6 h-6 overflow-hidden border-2 border-white rounded-full dark:border-gray-900">
                                                <img src="./src/assets/country-01.svg" alt="user" />
                                            </div>
                                            <div className="w-6 h-6 overflow-hidden border-2 border-white rounded-full dark:border-gray-900">
                                                <img src="./src/assets/country-01.svg" alt="user" />
                                            </div>
                                        </div>
                                    </div>
                                </td>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <p className="rounded-full bg-success-50 px-2 py-0.5 text-theme-xs font-medium text-success-700 dark:bg-success-500/15 dark:text-success-500">
                                            Active
                                        </p>
                                    </div>
                                </td>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <p className="text-gray-500 text-theme-sm dark:text-gray-400">3.9K</p>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <div className="flex items-center gap-3">
                                            <div className="w-10 h-10 overflow-hidden rounded-full">
                                                <img src="./src/assets/country-02.svg" alt="brand" />
                                            </div>
                                            <div>
                                                <span className="block font-medium text-gray-800 text-theme-sm dark:text-red-500">
                                                    Kaiya George
                                                </span>
                                                <span className="block text-gray-500 text-theme-xs dark:text-gray-400">
                                                    Project Manager
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <p className="text-gray-500 text-theme-sm dark:text-gray-400">
                                            Technology
                                        </p>
                                    </div>
                                </td>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <div className="flex -space-x-2">
                                            <div className="w-6 h-6 overflow-hidden border-2 border-white rounded-full dark:border-gray-900">
                                                <img src="./src/assets/country-02.svg" alt="user" />
                                            </div>
                                            <div className="w-6 h-6 overflow-hidden border-2 border-white rounded-full dark:border-gray-900">
                                                <img src="./src/assets/country-02.svg" alt="user" />
                                            </div>
                                        </div>
                                    </div>
                                </td>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <p className="rounded-full bg-warning-50 px-2 py-0.5 text-theme-xs font-medium text-warning-700 dark:bg-warning-500/15 dark:text-warning-400">
                                            Pending
                                        </p>
                                    </div>
                                </td>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <p className="text-gray-500 text-theme-sm dark:text-gray-400">
                                            24.9K
                                        </p>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <div className="flex items-center gap-3">
                                            <div className="w-10 h-10 overflow-hidden rounded-full">
                                                <img src="./src/assets/country-03.svg" alt="brand" />
                                            </div>
                                            <div>
                                                <span className="block font-medium text-gray-800 text-theme-sm dark:text-red-500">
                                                    Anh Nguyen
                                                </span>
                                                <span className="block text-gray-500 text-theme-xs dark:text-gray-400">
                                                    Digital Marketer
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <p className="text-gray-500 text-theme-sm dark:text-gray-400">
                                            Social Media
                                        </p>
                                    </div>
                                </td>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <div className="flex -space-x-2">
                                            <div className="w-6 h-6 overflow-hidden border-2 border-white rounded-full dark:border-gray-900">
                                                <img src="./src/assets/country-03.svg" alt="user" />
                                            </div>
                                            <div className="w-6 h-6 overflow-hidden border-2 border-white rounded-full dark:border-gray-900">
                                                <img src="./src/assets/country-02.svg" alt="user" />
                                            </div>
                                            <div className="w-6 h-6 overflow-hidden border-2 border-white rounded-full dark:border-gray-900">
                                                <img src="./src/assets/country-01.svg" alt="user" />
                                            </div>
                                        </div>
                                    </div>
                                </td>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <p className="rounded-full bg-error-50 px-2 py-0.5 text-theme-xs font-medium text-error-700 dark:bg-error-500/15 dark:text-error-500">
                                            Cancel
                                        </p>
                                    </div>
                                </td>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <p className="text-gray-500 text-theme-sm dark:text-gray-400">2.8K</p>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <div className="flex items-center gap-3">
                                            <div className="w-10 h-10 overflow-hidden rounded-full">
                                                <img src="./src/assets/country-01.svg" alt="brand" />
                                            </div>
                                            <div>
                                                <span className="block font-medium text-gray-800 text-theme-sm dark:text-red-500">
                                                    Carla George
                                                </span>
                                                <span className="block text-gray-500 text-theme-xs dark:text-gray-400">
                                                    Front-end Developer
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <p className="text-gray-500 text-theme-sm dark:text-gray-400">
                                            Website
                                        </p>
                                    </div>
                                </td>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <div className="flex -space-x-2">
                                            <div className="w-6 h-6 overflow-hidden border-2 border-white rounded-full dark:border-gray-900">
                                                <img src="./src/assets/country-01.svg" alt="user" />
                                            </div>
                                            <div className="w-6 h-6 overflow-hidden border-2 border-white rounded-full dark:border-gray-900">
                                                <img src="./src/assets/country-01.svg" alt="user" />
                                            </div>
                                            <div className="w-6 h-6 overflow-hidden border-2 border-white rounded-full dark:border-gray-900">
                                                <img src="./src/assets/country-01.svg" alt="user" />
                                            </div>
                                        </div>
                                    </div>
                                </td>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <p className="rounded-full bg-success-50 px-2 py-0.5 text-theme-xs font-medium text-success-700 dark:bg-success-500/15 dark:text-success-500">
                                            Active
                                        </p>
                                    </div>
                                </td>
                                <td className="px-5 py-4 sm:px-6">
                                    <div className="flex items-center">
                                        <p className="text-gray-500 text-theme-sm dark:text-gray-400">4,5K</p>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </>
    );
}