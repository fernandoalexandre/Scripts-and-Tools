#! /usr/bin/env python
#
#   EVEMineralCost - EVE Online script to grab mineral prices from eve-central
#           and calculate how much a certain quantity of minerals cost based on
#           those prices.
#
#   Copyright (C) 2009  Fernando Alexandre
#
#   This program is free software: you can redistribute it and/or modify
#   it under the terms of the GNU General Public License as published by
#   the Free Software Foundation, either version 3 of the License, or
#   (at your option) any later version.
#
#   This program is distributed in the hope that it will be useful,
#   but WITHOUT ANY WARRANTY; without even the implied warranty of
#   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#   GNU General Public License for more details.
#
#   You should have received a copy of the GNU General Public License
#   along with this program.  If not, see <http://www.gnu.org/licenses/>.
import urllib2
from xml.dom import minidom

source = 'http://api.eve-central.com/api/evemon'
prices = []
names = []
results = []

# Downloads the Mineral prices from <source>, processes the XML and saves the
# prices and names to <prices> and <names> lists.
def grab_mineral_prices():
    print 'Grabbing prices from %s' % source
    conn = urllib2.urlopen(source)
    xml = minidom.parseString(conn.read())
    print 'Using the current rates:'
    for node in xml.getElementsByTagName('mineral'):
        name = node.getElementsByTagName('name')[0].childNodes[0].data
        try:
            price = float(node.getElementsByTagName('price')[0].childNodes[0].data)
        except:
            print 'Error converting %s price to float.' % name
            sys.exit(0)
        names.append(name)
        prices.append(price)
        print '%s : %.3f ISK' % (name, price)
    return

# Gathers the user's amount of minerals for an expense calculation.
def process():
    print '\nEnter the quantities:\n'
    for i in range(0, len(names)):
        prompt = '%s amount: ' % names[i]
        try:
            val = int(raw_input(prompt))
        except ValueError:
            val = 0
        results.append(prices[i] * val)
    return

# Prints the results.
def print_results():
    print "\nResults:"
    for i in range(0, len(names)):
        print "%s:   %.2f" % (names[i], results[i])
    return

# Sums all the elements on the list <results> and returns the result.
def getTotal():
    result = 0.0;
    for i in results:
        result += i
    return result

def main():
    grab_mineral_prices()
    process()
    print_results()
    print 'Total:   %.2f ISK' % getTotal()

if __name__ == "__main__":
    main()