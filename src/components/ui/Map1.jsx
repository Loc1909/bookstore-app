import React, { useState, useEffect, useRef } from 'react';
import jsVectorMap from 'jsvectormap';
import 'jsvectormap/dist/maps/world.js';
import "jsvectormap/dist/jsvectormap.min.css";
import "../../js/world.js";
const Map1 = () => {
    const [openDropDown, setOpenDropDown] = useState(false);
    useEffect(() => {
        const map = new jsVectorMap({
            selector: '#mapOne',
            map: 'world',
            visualizeData: {
                scale: ['#eeeeee', '#3c50e0'],
                values: {
                    US: 2379,
                    FR: 589,
                }
            },
        });
        return () => {
            const mapElement = document.getElementById('mapOne');
            if (mapElement) mapElement.innerHTML = '';
        };
    }, []);
    return (
        <div className="rounded-2xl border border-gray-200 bg-white p-5 dark:border-gray-300 dark:bg-white/3 sm:p-6">
            <div className="flex justify-between">
                <div>
                    <h3 className="text-lg font-semibold text-gray-800 dark:text-red-400">
                        Customers Demographic
                    </h3>
                    <p className="mt-1 text-theme-sm text-gray-500 dark:text-gray-400">
                        Number of customer based on country
                    </p>
                </div>

                <div className="relative h-fit">
                    <button
                        onClick={() => setOpenDropDown(!openDropDown)}
                        className={
                            openDropDown
                                ? 'text-gray-700 dark:text-white'
                                : 'text-gray-400 hover:text-gray-700 dark:hover:text-white'
                        }
                    >
                        <svg
                            className="fill-current"
                            width="24"
                            height="24"
                            viewBox="0 0 24 24"
                            fill="none"
                            xmlns="http://www.w3.org/2000/svg"
                        >
                            <path
                                fillRule="evenodd"
                                clipRule="evenodd"
                                d="M10.2441 6C10.2441 5.0335 11.0276 4.25 11.9941 4.25H12.0041C12.9706 4.25 13.7541 5.0335 13.7541 6C13.7541 6.9665 12.9706 7.75 12.0041 7.75H11.9941C11.0276 7.75 10.2441 6.9665 10.2441 6ZM10.2441 18C10.2441 17.0335 11.0276 16.25 11.9941 16.25H12.0041C12.9706 16.25 13.7541 17.0335 13.7541 18C13.7541 18.9665 12.9706 19.75 12.0041 19.75H11.9941C11.0276 19.75 10.2441 18.9665 10.2441 18ZM11.9941 10.25C11.0276 10.25 10.2441 11.0335 10.2441 12C10.2441 12.9665 11.0276 13.75 11.9941 13.75H12.0041C12.9706 13.75 13.7541 12.9665 13.7541 12C13.7541 11.0335 12.9706 10.25 12.0041 10.25H11.9941Z"
                                fill="currentColor"
                            />
                        </svg>
                    </button>

                    {/* Dropdown Menu */}
                    {openDropDown && (
                        <div
                            onBlur={() => setOpenDropDown(false)}
                            className="absolute right-0 top-full z-40 w-40 space-y-1 rounded-2xl border border-gray-200 bg-white p-2 shadow-theme-lg dark:border-gray-300 dark:bg-gray-dark"
                        >
                            <button className="flex w-full rounded-lg px-3 py-2 text-left text-theme-xs font-medium text-gray-500 hover:bg-gray-100 hover:text-gray-700 dark:text-gray-400 dark:hover:bg-white/5 dark:hover:text-gray-300">
                                View More
                            </button>
                            <button className="flex w-full rounded-lg px-3 py-2 text-left text-theme-xs font-medium text-gray-500 hover:bg-gray-100 hover:text-gray-700 dark:text-gray-400 dark:hover:bg-white/5 dark:hover:text-gray-300">
                                Delete
                            </button>
                        </div>
                    )}
                </div>
            </div>

            <div className="my-6 overflow-hidden rounded-2xl border border-gray-200 bg-gray-50 px-4 py-6 dark:border-gray-300 dark:bg-gray-900 sm:px-6">
                <div
                    id="mapOne"
                    className="mapOne map-btn -mx-4 -my-6 h-53 w-63 2xsm:w-[307px] xsm:w-[358px] sm:-mx-6 md:w-167 lg:w-158.5 xl:w-98.25 2xl:w-138.5"
                ></div>
            </div>

            <div className="space-y-5">
                {/* USA Row */}
                <div className="flex items-center justify-between">
                    <div className="flex items-center gap-3">
                        <div className="w-full max-w-8 items-center rounded-full">
                            <img src="./src/assets/country-01.svg" alt="usa" />
                        </div>
                        <div>
                            <p className="text-theme-sm font-semibold text-gray-800 dark:text-red-500">
                                USA
                            </p>
                            <span className="block text-theme-xs text-gray-500 dark:text-gray-400">
                                2,379 Customers
                            </span>
                        </div>
                    </div>

                    <div className="flex w-full max-w-35 items-center gap-3">
                        <div className="relative block h-2 w-full max-w-25 rounded-sm bg-gray-200 dark:bg-gray-800">
                            <div
                                className="absolute left-0 top-0 flex h-full items-center justify-center rounded-sm bg-brand-500 text-xs font-medium text-white"
                                style={{ width: '79%' }}
                            ></div>
                        </div>
                        <p className="text-theme-sm font-medium text-gray-800 dark:text-red-500">
                            79%
                        </p>
                    </div>
                </div>

                {/* France Row */}
                <div className="flex items-center justify-between">
                    <div className="flex items-center gap-3">
                        <div className="w-full max-w-8 items-center rounded-full">
                            <img src="./src/assets/country-02.svg" alt="france" />
                        </div>
                        <div>
                            <p className="text-theme-sm font-semibold text-gray-800 dark:text-red-500">
                                France
                            </p>
                            <span className="block text-theme-xs text-gray-500 dark:text-gray-400">
                                589 Customers
                            </span>
                        </div>
                    </div>

                    <div className="flex w-full max-w-35 items-center gap-3">
                        <div className="relative block h-2 w-full max-w-25 rounded-sm bg-gray-200 dark:bg-gray-800">
                            <div
                                className="absolute left-0 top-0 flex h-full items-center justify-center rounded-sm bg-brand-500 text-xs font-medium text-white"
                                style={{ width: '23%' }}
                            ></div>
                        </div>
                        <p className="text-theme-sm font-medium text-gray-800 dark:text-red-500">
                            23%
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Map1;